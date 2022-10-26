package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.entities.OrderDetail;
import com.study.reggie.mapper.OrderDetailMapper;
import com.study.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author That's all
 * @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
 * @createDate 2022-10-18 02:40:25
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
        implements OrderDetailService {

}




