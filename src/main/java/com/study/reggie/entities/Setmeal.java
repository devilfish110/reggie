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
 * 套餐
 *
 * @author That's all
 * @TableName setmeal
 */
@TableName(value = "setmeal")
@Data
public class Setmeal implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键：套餐id
     */
    @TableId(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 菜品分类id
     */
    @TableField(value = "category_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;
    /**
     * 套餐名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 套餐价格
     */
    @TableField(value = "price")
    private BigDecimal price;
    /**
     * 状态 0:停用 1:启用
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 编码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 描述信息
     */
    @TableField(value = "description")
    private String description;
    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;
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
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;
}