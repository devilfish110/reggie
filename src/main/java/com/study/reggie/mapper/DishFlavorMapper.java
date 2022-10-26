package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
 * @createDate 2022-10-15 13:46:41
 * @Entity com.study.reggie.entities.DishFlavor
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




