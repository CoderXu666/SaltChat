package com.quick.api.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatSessionMapper;
import com.quick.api.service.QuickChatSessionService;
import com.quick.common.adapter.SessionAdapter;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.enums.SessionTypeEnum;
import com.quick.common.enums.YesNoEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.pojo.po.QuickChatGroup;
import com.quick.common.pojo.po.QuickChatSession;
import com.quick.common.pojo.po.QuickChatUser;
import com.quick.common.pojo.vo.ChatSessionVO;
import com.quick.api.store.QuickChatGroupStore;
import com.quick.api.store.QuickChatMsgStore;
import com.quick.api.store.QuickChatSessionStore;
import com.quick.api.store.QuickChatUserStore;
import com.quick.common.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 聊天会话（针对单聊） 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatSessionServiceImpl extends ServiceImpl<QuickChatSessionMapper, QuickChatSession> implements QuickChatSessionService {
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Value("${quick-chat.session-size-limit}")
    private int sessionSizeLimit;

    @Override
    public List<ChatSessionVO> getSessionList() {
        // 查询会话列表，超过50个会话：超出部分已读直接干掉
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(loginAccountId);
        if (CollectionUtils.isEmpty(sessionList)) {
            return new ArrayList<>();
        }
        sessionList = sessionList.stream().distinct().collect(Collectors.toList());
        if (sessionList.size() > sessionSizeLimit) {
            List<QuickChatSession> subSessionList = sessionList.subList(sessionSizeLimit - 1, sessionList.size());
            List<QuickChatSession> over50List = subSessionList.stream()
                    .filter(item -> item.getLastReadTime().isAfter(item.getUpdateTime()))
                    .collect(Collectors.toList());
            sessionList.removeAll(over50List);
        }

        // 单聊：会话列表
        Map<Integer, List<QuickChatSession>> sessionListMap = sessionList.stream()
                .collect(Collectors.groupingBy(QuickChatSession::getType));
        List<QuickChatUser> users = new ArrayList<>();
        List<QuickChatSession> singleList = sessionListMap.get(SessionTypeEnum.SINGLE.getCode());
        if (CollectionUtils.isNotEmpty(singleList)) {
            List<String> accountIds = singleList.stream()
                    .map(QuickChatSession::getToId)
                    .collect(Collectors.toList());
            users = userStore.getListByAccountIds(accountIds);
        }

        // 群聊：会话列表
        List<QuickChatGroup> groups = new ArrayList<>();
        List<QuickChatSession> groupList = sessionListMap.get(SessionTypeEnum.GROUP.getCode());
        if (CollectionUtils.isNotEmpty(groupList)) {
            List<String> groupIds = groupList.stream()
                    .map(QuickChatSession::getToId)
                    .collect(Collectors.toList());
            groups = groupStore.getListByGroupIds(groupIds);
        }

        // 查询会话未读数量
        List<ChatSessionVO> sessionVOList = SessionAdapter.buildSessionVOList(sessionList, users, groups);
        Map<Long, Long> unreadCountMap = this.getUnreadCountMap(sessionVOList);
        for (ChatSessionVO sessionVO : sessionVOList) {
            Long relationId = sessionVO.getRelationId();
            if (unreadCountMap.containsKey(relationId)) {
                Long unreadCount = unreadCountMap.get(relationId);
                sessionVO.setUnreadCount(unreadCount);
            }
        }
        return sessionVOList;
    }

    @Override
    public void deleteSession(Long sessionId) {
        QuickChatSession session = sessionStore.getBySessionId(sessionId);
        if (ObjectUtils.isEmpty(session)) {
            throw new QuickException(ResponseEnum.SESSION_NOT_EXIST);
        }
        session.setTopFlag(YesNoEnum.NO.getCode());
        session.setStatus(YesNoEnum.NO.getCode());
        sessionStore.updateSessionById(session);
    }

    @Override
    public void updateReadTime(Long sessionId) {
        QuickChatSession sessionPO = SessionAdapter.buildSessionPO(sessionId, LocalDateTime.now());
        sessionStore.updateSessionById(sessionPO);
    }

    @Override
    public Map<Long, Long> getUnreadCountMap(List<ChatSessionVO> sessionList) {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        Map<Long, Long> resultMap = new HashMap<>();
        for (ChatSessionVO session : sessionList) {
            Long relationId = session.getRelationId();
            Long unreadCount = msgStore.getUnreadCount(loginAccountId, relationId, session.getLastReadTime());
            unreadCount = unreadCount == 0 ? null : unreadCount;
            resultMap.put(relationId, unreadCount);
        }
        return resultMap;
    }

    @Override
    public ChatSessionVO getByFromIdAndToId(String fromId, String toId) {
        QuickChatSession sessionPO = sessionStore.getByFromIdAndToId(fromId, toId);
        if (ObjectUtils.isEmpty(sessionPO)) {
            return null;
        }
        ChatSessionVO sessionVO = null;
        if (SessionTypeEnum.SINGLE.getCode().equals(sessionPO.getType())) {
            QuickChatUser userPO = userStore.getByAccountId(sessionPO.getToId());
            sessionVO = SessionAdapter.buildUserSessionPO(userPO, sessionPO);
        } else {
            QuickChatGroup groupPO = groupStore.getByGroupId(Long.valueOf(sessionPO.getToId()));
            sessionVO = SessionAdapter.buildGroupSessionPO(groupPO, sessionPO);
        }
        Map<Long, Long> unreadCountMap = this.getUnreadCountMap(ListUtil.of(sessionVO));
        Long unreadCount = unreadCountMap.get(sessionVO.getRelationId());
        sessionVO.setUnreadCount(unreadCount);
        return sessionVO;
    }

    @Override
    public void topSession(Long sessionId) {
        QuickChatSession sessionPO = sessionStore.getBySessionId(sessionId);
        if (ObjectUtils.isEmpty(sessionPO)) {
            throw new QuickException(ResponseEnum.SESSION_NOT_EXIST);
        }
        sessionPO.setStatus(YesNoEnum.YES.getCode());
        sessionPO.setTopFlag(YesNoEnum.YES.getCode());
        sessionStore.updateSessionById(sessionPO);
    }

    @Override
    public void activeSession(String toId) {
        String fromId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatSession sessionPO = sessionStore.getByFromIdAndToId(fromId, toId);
        if (ObjectUtils.isEmpty(sessionPO)) {
            throw new QuickException(ResponseEnum.SESSION_NOT_EXIST);
        }
        sessionPO.setStatus(YesNoEnum.YES.getCode());
        sessionPO.setTopFlag(YesNoEnum.NO.getCode());
        sessionPO.setUpdateTime(LocalDateTime.now());
        sessionStore.updateSessionById(sessionPO);
    }
}
