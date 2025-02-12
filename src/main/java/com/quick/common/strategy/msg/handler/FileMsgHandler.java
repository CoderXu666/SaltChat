package com.quick.common.strategy.msg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.common.adapter.MsgAdapter;
import com.quick.common.enums.ChatMsgEnum;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.pojo.dto.ChatMsgDTO;
import com.quick.common.pojo.dto.FileExtraDTO;
import com.quick.common.pojo.po.QuickChatMsg;
import com.quick.api.store.QuickChatMsgStore;
import com.quick.common.strategy.file.handler.FileHandler;
import com.quick.common.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:07
 * @Description: 文件消息（图片、视频等）
 * @Version: 1.0
 */
@Component
public class FileMsgHandler extends AbstractChatMsgStrategy {
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private FileHandler fileHandler;
    @Value("${quick-chat.size.file}")
    private Integer fileSize;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FILE;
    }

    @Override
    public QuickChatMsg sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 文件大小限制，不超过10MB
        FileExtraDTO extraInfo = msgDTO.getExtraInfo();
        long size = extraInfo.getSize() / 1024 / 1024;
        if (size > fileSize) {
            fileHandler.deleteFile(msgDTO.getContent());
            ResponseEnum responseEnum = ResponseEnum.FILE_OVER_SIZE;
            responseEnum.setMsg(String.format(responseEnum.getMsg(), fileSize + "MB"));
            throw new QuickException(responseEnum);
        }

        // 保存消息
        String fromId = msgDTO.getFromId();
        String toId = msgDTO.getToId();
        String nickName = msgDTO.getNickName();
        String fileUrl = msgDTO.getContent();
        Long relationId = msgDTO.getRelationId();
        QuickChatMsg chatMsg = MsgAdapter.buildChatMsgPO(fromId, toId, relationId, nickName,
                fileUrl, null, JSONUtil.toJsonStr(extraInfo), this.getEnum().getCode());
        msgStore.saveMsg(chatMsg);
        return chatMsg;
    }
}
