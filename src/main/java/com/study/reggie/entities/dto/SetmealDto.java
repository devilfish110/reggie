package com.study.reggie.entities.dto;

import com.study.reggie.entities.Setmeal;
import com.study.reggie.entities.SetmealDish;
import lombok.Data;

import java.util.List;


/**
 * @author That's all
 * setmeal表和setmeal_dish表的组合
 */
@Data
public class SetmealDto extends Setmeal {
    /**
     * 套餐口味列表
     */
    private List<SetmealDish> setmealDishes;
    /**
     * 套餐分类名称
     */
    private String categoryName;
}
