package com.study.reggie.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.entities.Dish;
import com.study.reggie.entities.Orders;
import com.study.reggie.entities.Setmeal;
import com.study.reggie.entities.dto.DishDto;
import com.study.reggie.entities.dto.OrderDto;
import com.study.reggie.entities.dto.SetmealDto;

/**
 * @author That's all
 * 作用于Object数据转为另一个Object数据
 * 例如:A-->(AB)Vo
 */
public class ObjectConversionObject {

    /**
     * dish转换为dishDto对象
     *
     * @param dish 数据源
     */
    public static DishDto convertDishVo(Dish dish) {
        if (dish == null) {
            return null;
        }
        DishDto dishDto = new DishDto();
        dishDto.setId(dish.getId());
        dishDto.setName(dish.getName());
        dishDto.setCategoryId(dish.getCategoryId());
        dishDto.setPrice(dish.getPrice());
        dishDto.setCode(dish.getCode());
        dishDto.setImage(dish.getImage());
        dishDto.setDescription(dish.getDescription());
        dishDto.setStatus(dish.getStatus());
        dishDto.setSort(dish.getSort());
        dishDto.setCreateTime(dish.getCreateTime());
        dishDto.setUpdateTime(dish.getUpdateTime());
        dishDto.setCreateUser(dish.getCreateUser());
        dishDto.setUpdateUser(dish.getUpdateUser());
        dishDto.setIsDeleted(dish.getIsDeleted());
        return dishDto;
    }

    /**
     * setmeal转为setmealDto对象
     *
     * @param setmeal 数据源
     * @return Vo
     */
    public static SetmealDto convertSetmealVo(Setmeal setmeal) {
        if (setmeal == null) {
            return null;
        }
        SetmealDto setmealDto = new SetmealDto();
        setmealDto.setId(setmeal.getId());
        setmealDto.setCategoryId(setmeal.getCategoryId());
        setmealDto.setName(setmeal.getName());
        setmealDto.setPrice(setmeal.getPrice());
        setmealDto.setStatus(setmeal.getStatus());
        setmealDto.setCode(setmeal.getCode());
        setmealDto.setDescription(setmeal.getDescription());
        setmealDto.setImage(setmeal.getImage());
        setmealDto.setCreateTime(setmeal.getCreateTime());
        setmealDto.setUpdateTime(setmeal.getUpdateTime());
        setmealDto.setCreateUser(setmeal.getCreateUser());
        setmealDto.setUpdateUser(setmeal.getUpdateUser());
        setmealDto.setIsDeleted(setmeal.getIsDeleted());
        return setmealDto;
    }

    /**
     * order转orderDto
     *
     * @param order 数据源
     * @return orderDto
     */
    public static OrderDto convertOrderVo(Orders order) {
        if (order == null) {
            return null;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setNumber(order.getNumber());
        orderDto.setStatus(order.getStatus());
        orderDto.setUserId(order.getUserId());
        orderDto.setAddressBookId(order.getAddressBookId());
        orderDto.setOrderTime(order.getOrderTime());
        orderDto.setCheckoutTime(order.getCheckoutTime());
        orderDto.setPayMethod(order.getPayMethod());
        orderDto.setAmount(order.getAmount());
        orderDto.setRemark(order.getRemark());
        orderDto.setPhone(order.getPhone());
        orderDto.setAddress(order.getAddress());
        orderDto.setUserName(order.getUserName());
        orderDto.setConsignee(order.getConsignee());
        return orderDto;
    }

    /**
     * page转为Vo的page
     *
     * @param page  数据源
     * @param <New> Vo的page类型
     * @param <Old> page类型
     * @return Vo的page
     */
    public static <New, Old> Page<New> convertPageVo(Page<Old> page, Page<New> newPage) {
        if (page == null) {
            return null;
        }
        newPage.setTotal(page.getTotal());
        //size 每页显示条数，默认 10
        newPage.setSize(page.getSize());
        //current 当前页,默认1
        newPage.setCurrent(page.getCurrent());
        //orders 排序字段信息
        newPage.setOrders(page.getOrders());
        //不设置records
        return newPage;
    }
}
