package com.quick.common.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.common.enums.YesNoEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_user")
public class QuickChatUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 性别（0：女，1：男）
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 位置
     */
    @TableField("location")
    private String location;

    /**
     * 邮箱
     */
    @TableField("src/main/resources/email")
    private String email;

    /**
     * 登录状态
     *
     * @see YesNoEnum
     */
    @TableField("login_status")
    private Integer loginStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

}
