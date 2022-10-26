package com.study.reggie.entities.dto;

import com.study.reggie.entities.OrderDetail;
import com.study.reggie.entities.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author That's all
 */
@Data
public class OrderDto extends Orders {
    /**
     * 订单明细表
     */
    private List<OrderDetail> orderDetails;

    /**
     * 下单用户
     */
    private String name;
}
