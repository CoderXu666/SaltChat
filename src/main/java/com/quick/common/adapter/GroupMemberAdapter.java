package com.quick.common.adapter;

import com.quick.common.pojo.po.QuickChatGroupMember;

/**
 * @Author 徐志斌
 * @Date: 2024/1/8 21:22
 * @Version 1.0
 * @Description: 群成员适配器
 */
public class GroupMemberAdapter {
    public static QuickChatGroupMember buildMemberPO(Long groupId, String accountId) {
        QuickChatGroupMember groupMember = new QuickChatGroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setAccountId(accountId);
        return groupMember;
    }
}
