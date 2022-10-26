package com.study.reggie.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 *
 * @author That's all
 * @TableName orders
 */
@Data
@TableName(value = "orders")
public class Orders implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 订单id：主键
     */
    @TableId(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 订单号
     */
    @TableField(value = "number")
    private String number;
    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 下单用户:user_id
     */
    @TableField(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 地址id:address_Id
     */
    @TableField(value = "address_book_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long addressBookId;
    /**
     * 下单时间
     */
    @TableField(value = "order_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderTime;
    /**
     * 结账时间
     */
    @TableField(value = "checkout_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime checkoutTime;
    /**
     * 支付方式 1微信,2支付宝
     */
    @TableField(value = "pay_method")
    private Integer payMethod;
    /**
     * 实收金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 收货人手机号
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 收货地址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 下单人姓名
     */
    @TableField(value = "user_name")
    private String userName;
    /**
     * 收货人姓名
     */
    @TableField(value = "consignee")
    private String consignee;
}