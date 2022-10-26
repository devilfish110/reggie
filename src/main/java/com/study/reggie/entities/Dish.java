package com.study.reggie.entities;

import com.baomidou.mybatisplus.annotation.*;
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
 * 菜品实体类
 *
 * @author That's all
 * @TableName dish
 */
@TableName(value = "dish")
@Data
public class Dish implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键:dishId
     */
    @TableId(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 菜品名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 菜品分类id:categoryId
     */
    @TableField(value = "category_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;
    /**
     * 菜品价格
     */
    @TableField(value = "price")
    private BigDecimal price;
    /**
     * 商品码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;
    /**
     * 描述信息
     */
    @TableField(value = "description")
    private String description;
    /**
     * 0:停售 1:起售(default)
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 顺序(default:0)
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUser;
    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUser;
    /**
     * 是否删除(default:0)
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;
}