package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatGroupAdapter;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.service.QuickChatGroupService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Service
public class QuickChatGroupServiceImpl extends ServiceImpl<QuickChatGroupMapper, QuickChatGroup> implements QuickChatGroupService {
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createGroup(GroupDTO groupDTO) {
        // 保存群聊信息
        QuickChatGroup groupPO = ChatGroupAdapter.buildGroupPO(groupDTO);
        groupStore.saveGroup(groupPO);

        // 批量添加群成员
        List<String> accountIds = groupDTO.getAccountIdList();
        List<QuickChatGroupMember> memberList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountIds)) {
            for (String accountId : accountIds) {
                QuickChatGroupMember member = GroupMemberAdapter.buildMemberPO(groupPO.getGroupId(), accountId);
                memberList.add(member);
            }
            memberStore.saveMemberList(memberList);
        }

        // Channel 通知群内加入新成员
        return null;
    }
}
