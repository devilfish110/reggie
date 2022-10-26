package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【dish(菜品管理)】的数据库操作Mapper
 * @createDate 2022-10-14 22:18:18
 * @Entity com.study.reggie.entities.Dish
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




