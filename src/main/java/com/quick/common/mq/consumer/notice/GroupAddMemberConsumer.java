package com.quick.common.mq.consumer.notice;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.WsPushEnum;
import com.quick.common.netty.UserChannelRelation;
import com.quick.common.pojo.entity.WsPushEntity;
import com.quick.common.pojo.po.QuickChatApply;
import com.quick.common.pojo.po.QuickChatGroupMember;
import com.quick.api.store.QuickChatGroupMemberStore;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2024/10/6 14:42
 * @Version 1.0
 * @Description: 添加群成员-消费者
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.GROUP_ADD_MEMBER_NOTICE, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class GroupAddMemberConsumer implements RocketMQListener<Message<QuickChatApply>> {
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public void onMessage(Message<QuickChatApply> message) {
        QuickChatApply apply = message.getPayload();
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(apply.getGroupId());
        for (QuickChatGroupMember member : members) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
            if (ObjectUtils.isNotEmpty(channel)) {
                WsPushEntity<QuickChatApply> pushEntity = new WsPushEntity<>();
                pushEntity.setPushType(WsPushEnum.GROUP_ADD_MEMBER_NOTICE.getCode());
                pushEntity.setMessage(apply);
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
            }
        }
    }
}
