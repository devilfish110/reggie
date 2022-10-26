package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Mapper
 * @createDate 2022-10-16 15:06:53
 * @Entity com.study.reggie.entities.SetmealDish
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

}




