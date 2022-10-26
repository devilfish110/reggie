package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.entities.ShoppingCart;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.mapper.ShoppingCartMapper;
import com.study.reggie.service.ShoppingCartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author That's all
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
 * @createDate 2022-10-18 00:43:21
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {

    @Override
    public boolean saveCommodity(ShoppingCart commodity) {
        if (commodity != null) {
            //查询用户购物车有没有数据
            Long userId = commodity.getUserId();
            Long dishId = commodity.getDishId();
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            //添加的是dish
            if (dishId != null) {
                String dishFlavor = commodity.getDishFlavor();
                //查询是否存在相同的dish及flavor
                queryWrapper.eq(ShoppingCart::getUserId, userId)
                        .eq(ShoppingCart::getDishId, dishId)
                        .eq(dishFlavor != null, ShoppingCart::getDishFlavor, dishFlavor);
                return addCart(commodity, queryWrapper);
            }
            Long setmealId = commodity.getSetmealId();
            //添加的是setmeal
            if (setmealId != null) {
                //查询是否存在相同的setmeal
                queryWrapper.eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getSetmealId, setmealId);
                return addCart(commodity, queryWrapper);
            }
        }
        return false;
    }

    /**
     * 添加商品到购物车时出现的重复代码(添加dish或setmeal)
     *
     * @param commodity
     * @param queryWrapper
     * @return
     */
    private boolean addCart(ShoppingCart commodity, LambdaQueryWrapper<ShoppingCart> queryWrapper) {
        List<ShoppingCart> list = super.list(queryWrapper);
        if (list != null && list.size() > 0) {
            //存在相同的数据,在原来的基础上 + commodity.getNumber()
            list.forEach(cart -> {
                cart.setNumber(cart.getNumber() + 1);
                super.updateById(cart);
            });
            return true;
        } else {
            //不存在相同的数据
            return super.save(commodity);
        }
    }

    @Override
    public List<ShoppingCart> getCartListByUserId(Long userId) {
        if (userId != null) {
            //商品数量为0不查询返回
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getUserId, userId)
                    .ne(ShoppingCart::getNumber, 0)
                    .orderByDesc(ShoppingCart::getCreateTime);
            return super.list(queryWrapper);
        }
        return null;
    }


    @Override
    public boolean removeCommodity(ShoppingCart commodity) throws CustomerException {
        if (commodity != null) {
            Long userId = commodity.getUserId();
            Long dishId = commodity.getDishId();
            String dishFlavor = commodity.getDishFlavor();
            Long setmealId = commodity.getSetmealId();
            if (userId != null) {
                LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ShoppingCart::getUserId, userId)
                        .eq(dishId != null, ShoppingCart::getDishId, dishId)
                        .eq(StringUtils.isNotBlank(dishFlavor), ShoppingCart::getDishFlavor, dishFlavor)
                        .eq(setmealId != null, ShoppingCart::getSetmealId, setmealId);
                //判断删除dish还是setmeal
                if (dishId != null) {
                    //删除dish，米饭的dish可能为null
                    List<ShoppingCart> list = super.list(queryWrapper);
                    return super.removeByIds(list);
                }
                if (setmealId != null) {
                    //删除setmeal
                    List<ShoppingCart> list = super.list(queryWrapper);
                    return super.removeByIds(list);
                }
            } else {
                throw new CustomerException("您暂未登录，请登录后再删除");
            }
        }
        return false;
    }


    @Override
    public boolean removeAllCommodityByUserId(Long userId) throws CustomerException {
        if (userId != null) {
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getUserId, userId);
            List<ShoppingCart> list = super.list(queryWrapper);
            return super.removeByIds(list);
        } else {
            throw new CustomerException("您暂未登录，请登陆后清空购物车");
        }
    }
}




