package com.quick.api.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.common.pojo.po.QuickChatGroupMember;

import java.util.List;

/**
 * <p>
 * 群成员 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
public interface QuickChatGroupMemberStore extends IService<QuickChatGroupMember> {

    /**
     * 根据 group_id 查询群成员列表
     *
     * @param groupId 群聊id
     * @return 群成员列表
     */
    List<QuickChatGroupMember> getListByGroupId(Long groupId);

    /**
     * 进入群聊
     *
     * @param memberPO 群成员信息
     * @return 执行结果
     */
    Boolean saveMember(QuickChatGroupMember memberPO);

    /**
     * 根据 group_id account_id 移出群聊
     *
     * @param groupId   群聊id
     * @param accountId 账号id
     * @return 执行结果
     */
    Boolean deleteByGroupIdAndAccountId(Long groupId, String accountId);

    /**
     * 查询成员否在群中
     *
     * @param groupId   群组id
     * @param accountId 账户id
     * @return 群成员信息
     */
    QuickChatGroupMember getMemberByAccountId(Long groupId, String accountId);

    /**
     * 根据 groupId，accountIdList 查询成员
     *
     * @param groupId       群id
     * @param accountIdList 成员id列表
     * @return
     */
    List<QuickChatGroupMember> getGroupMemberByAccountIdList(Long groupId, List<String> accountIdList);
}
