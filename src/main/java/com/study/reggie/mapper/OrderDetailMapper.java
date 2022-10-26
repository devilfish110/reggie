package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
 * @createDate 2022-10-18 02:40:25
 * @Entity com.study.reggie.entities.OrderDetail
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




