package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.ShoppingCart;
import com.study.reggie.exception.CustomerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author That's all
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service
 * @createDate 2022-10-18 00:43:21
 */
@Transactional(rollbackFor = Exception.class)
public interface ShoppingCartService extends IService<ShoppingCart> {


    /**
     * 用户添加商品到购物车
     *
     * @param commodity 商品信息(dish和setmeal)
     * @return 添加结果
     */
    boolean saveCommodity(ShoppingCart commodity);

    /**
     * 查询userId下的购物车信息
     *
     * @param userId 用户ID
     * @return list列表
     */
    List<ShoppingCart> getCartListByUserId(Long userId);

    /**
     * 移除商品
     *
     * @param commodity (dish_id和setmeal_id)的封装信息
     * @return 移除结果
     * @throws CustomerException 自定义异常
     */
    boolean removeCommodity(ShoppingCart commodity) throws CustomerException;

    /**
     * 清空user的购物车
     *
     * @param userId 用户id
     * @return 清空结果
     * @throws CustomerException 自定义异常
     */
    boolean removeAllCommodityByUserId(Long userId) throws CustomerException;
}
