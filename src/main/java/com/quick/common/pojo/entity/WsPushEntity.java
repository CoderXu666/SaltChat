package com.quick.common.pojo.entity;

import com.quick.common.enums.WsPushEnum;
import lombok.Data;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-11  11:37
 * @Description: WebSocket推送消息实体
 * @Version: 1.0
 */
@Data
public class WsPushEntity<T> {
    /**
     * 推送消息类型
     *
     * @see WsPushEnum
     */
    private Integer pushType;

    /**
     * 消息内容
     */
    private T message;
}
