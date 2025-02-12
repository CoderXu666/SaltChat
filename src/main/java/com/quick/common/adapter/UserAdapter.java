package com.quick.common.adapter;

import com.quick.common.pojo.po.QuickChatUser;
import com.quick.common.pojo.vo.ChatUserVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:03
 * @Version 1.0
 * @Description: 用户适配器
 */
public class UserAdapter {
    public static ChatUserVO buildUserVO(QuickChatUser userPO) {
        ChatUserVO userVO = new ChatUserVO();
        userVO.setAccountId(userPO.getAccountId());
        userVO.setNickName(userPO.getNickName());
        userVO.setGender(userPO.getGender());
        userVO.setLocation(userPO.getLocation());
        userVO.setCreateTime(userPO.getCreateTime());
        return userVO;
    }

    public static QuickChatUser buildUserPO(String accountId, String avatar, String password, Integer gender,
                                            String email, String location, Integer lineStatus) {
        QuickChatUser user = new QuickChatUser();
        user.setAccountId(accountId);
        user.setNickName(accountId);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setGender(gender);
        user.setEmail(email);
        user.setLocation(location);
        user.setLoginStatus(lineStatus);
        return user;
    }

    public static QuickChatUser buildUserPO(String accountId, String nickName, String avatar, Integer gender) {
        QuickChatUser user = new QuickChatUser();
        user.setAccountId(accountId);
        user.setNickName(nickName);
        user.setAvatar(avatar);
        user.setGender(gender);
        return user;
    }

    public static List<ChatUserVO> buildUserVOList(List<QuickChatUser> userList) {
        List<ChatUserVO> result = new ArrayList<>();
        for (QuickChatUser userPO : userList) {
            ChatUserVO userVO = new ChatUserVO();
            userVO.setAccountId(userPO.getAccountId());
            userVO.setNickName(userPO.getNickName());
            userVO.setGender(userPO.getGender());
            userVO.setEmail(userPO.getEmail());
            userVO.setLocation(userPO.getLocation());
            userVO.setCreateTime(userPO.getCreateTime());
            result.add(userVO);
        }
        return result;
    }
}
