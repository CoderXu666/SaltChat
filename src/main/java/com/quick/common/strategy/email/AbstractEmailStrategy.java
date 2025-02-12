package com.quick.common.strategy.email;

import com.quick.common.enums.EmailEnum;
import com.quick.common.pojo.dto.EmailDTO;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:58
 * @Description: 邮件策略抽象类
 * @Version: 1.0
 */
@Component
public abstract class AbstractEmailStrategy {
    /**
     * 策略 Handler 注册到策略工厂
     */
    @PostConstruct
    private void initStrategyHandler() {
        EmailStrategyFactory.register(this.getEnum().getType(), this);
    }

    /**
     * 获取当前实现类对应 Handler 枚举
     */
    protected abstract EmailEnum getEnum();

    /**
     * 发送邮件
     */
    public abstract void sendEmail(EmailDTO emailDTO) throws IOException, MessagingException, javax.mail.MessagingException;
}
