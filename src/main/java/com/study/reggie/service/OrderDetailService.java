package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.OrderDetail;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author That's all
 * @description 针对表【order_detail(订单明细表)】的数据库操作Service
 * @createDate 2022-10-18 02:40:25
 */
@Transactional(rollbackFor = Exception.class)
public interface OrderDetailService extends IService<OrderDetail> {

}
