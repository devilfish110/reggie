package com.study.reggie.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息
 *
 * @author That's all
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;
    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 邮箱地址
     */
    @TableField(value = "email")
    private String email;
    /**
     * 性别
     */
    @TableField(value = "sex")
    private String sex;
    /**
     * 身份证号
     */
    @TableField(value = "id_number")
    private String idNumber;
    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;
    /**
     * 状态 0:禁用，1:正常
     */
    @TableField(value = "status")
    private Integer status;
}