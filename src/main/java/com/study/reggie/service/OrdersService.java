package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.Orders;
import com.study.reggie.entities.dto.OrderDto;
import com.study.reggie.exception.CustomerException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author That's all
 * @description 针对表【orders(订单表)】的数据库操作Service
 * @createDate 2022-10-18 02:36:43
 */
@Transactional(rollbackFor = Exception.class)
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param orders 备注，支付方式，地址id
     * @return 返回结果
     * @throws CustomerException 自定义处理异常
     */
    boolean submit(Orders orders) throws CustomerException;

    /**
     * 查询用户的订单
     *
     * @param page page
     * @param id   下单用户id
     * @return Page
     * @throws CustomerException 自定义异常处理
     */
    Page<OrderDto> getPageById(Page<Orders> page, Long id) throws CustomerException;

    /**
     * 用户再来一单(只有userid,orders_id)
     *
     * @param order orders信息
     * @return 添加标志
     */
    boolean againOrders(Orders order);

    /**
     * 后端获取订单明细
     *
     * @param page      page
     * @param number    order_id
     * @param beginTime ge(>=开始时间)
     * @param endTime   le(<=结束时间)
     * @return pageInfo
     */
    Page<OrderDto> getPages(Page<Orders> page, String number, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 修改订单的状态
     *
     * @param order 封装了orders的主键和新状态
     * @return 修改结果
     */
    boolean updateStatus(Orders order);
}
