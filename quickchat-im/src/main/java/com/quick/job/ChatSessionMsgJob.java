package com.quick.job;

import com.quick.pojo.po.QuickChatMsg;
import com.quick.service.QuickChatMsgService;
import com.quick.store.doris.QuickChatMsgDorisStore;
import com.quick.store.mysql.QuickChatMsgStore;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 13:44
 * @Version 1.0
 * @Description: 会话、聊天信息定时 Job
 */
@Slf4j
@Component
public class ChatSessionMsgJob {
    @Autowired
    private QuickChatMsgDorisStore msgDorisStore;
    @Autowired
    private QuickChatMsgStore msgStore;

    /**
     * 保留30日聊天记录（凌晨 2：00 迁移到 Doris）
     */
    @XxlJob("MoveHistoryMsgToDorisJob")
    public ReturnT moveHistoryMsgToDorisJob() {
        // 查询 30 日内所有聊天记录信息
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.minusDays(30);
        List<QuickChatMsg> msgList = msgStore.getMsgByTime(startTime, endTime);
        Boolean isSuccess = msgDorisStore.saveBatchMsg(msgList);
        if (isSuccess) {

        }
        return ReturnT.SUCCESS;
    }
}
