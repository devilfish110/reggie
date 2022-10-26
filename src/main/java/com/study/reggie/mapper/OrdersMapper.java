package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【orders(订单表)】的数据库操作Mapper
 * @createDate 2022-10-18 02:36:43
 * @Entity com.study.reggie.entities.Orders
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




