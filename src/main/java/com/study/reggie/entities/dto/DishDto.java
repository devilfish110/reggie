package com.study.reggie.entities.dto;

import com.study.reggie.entities.Dish;
import com.study.reggie.entities.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * @author That's all
 * dish,category,dishFlavor的组合VO
 */
@Data
public class DishDto extends Dish {
    /*
     * 菜品口味列表
     */
    private List<DishFlavor> flavors = new ArrayList<>();
    /**
     * 菜品分类的名
     */
    private String categoryName;
}
