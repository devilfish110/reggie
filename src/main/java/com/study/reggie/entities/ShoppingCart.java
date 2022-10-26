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
 * 购物车
 *
 * @author That's all
 * @TableName shopping_cart
 */
@Data
@TableName(value = "shopping_cart")
public class ShoppingCart implements Serializable {
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
     * 名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;
    /**
     * user主键userId
     */
    @TableField(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
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
    /**
     * 创建时间,没有updateTime，所以不能自动填充
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}