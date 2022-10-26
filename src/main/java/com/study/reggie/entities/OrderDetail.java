package com.study.reggie.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细表
 *
 * @author That's all
 * @TableName order_detail
 */
@Data
@TableName(value = "order_detail")
public class OrderDetail implements Serializable {
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
     * 名字
     */
    @TableField(value = "name")
    private String name;
    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;
    /**
     * 订单id
     */
    @TableField(value = "order_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    /**
     * 菜品id
     */
    @TableField(value = "dish_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dishId;
    /**
     * 套餐id
     */
    @TableField(value = "setmeal_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long setmealId;
    /**
     * 口味
     */
    @TableField(value = "dish_flavor")
    private String dishFlavor;
    /**
     * 数量
     */
    @TableField(value = "number")
    private Integer number;
    /**
     * 金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;
}