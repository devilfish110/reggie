package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
 * @createDate 2022-10-18 00:43:21
 * @Entity com.study.reggie.entities.ShoppingCart
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




