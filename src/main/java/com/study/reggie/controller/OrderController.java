package com.study.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;
import com.study.reggie.entities.Orders;
import com.study.reggie.entities.dto.OrderDto;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService orderService;

    /**
     * 用户下单
     *
     * @param orders  信息
     * @param session 获取userid
     * @return 下单结果
     * @throws CustomerException 自定义异常处理
     */
    @PostMapping("/submit")
    public R<String> submitOrders(@RequestBody Orders orders, HttpSession session) throws CustomerException {
        Long userId = (Long) session.getAttribute("user");
        if (orders != null && userId != null) {
            orders.setUserId(userId);
            boolean flag = orderService.submit(orders);
            return flag ? R.success("下单成功") : R.error("下单失败");
        }
        return R.error("请登录后下单");
    }

    /**
     * 获取用户的订单信息page
     *
     * @param page    查看page页
     * @param pageNum 每页显示的数据量
     * @param session 获取用户id
     * @return page
     * @throws CustomerException 自定义异常处理
     */
    @GetMapping("/userPage")
    public R<Page<OrderDto>> getUserOrders(@RequestParam(value = "page") Integer page, @RequestParam(value = "pageSize") Integer pageNum, HttpSession session) throws CustomerException {
        Long userId = (Long) session.getAttribute("user");
        Page<Orders> ordersPage = new Page<>(page, pageNum);
        Page<OrderDto> pageById = orderService.getPageById(ordersPage, userId);
        return pageById != null ? R.success(pageById) : R.error("用户订单查询失败");
    }

    /**
     * 再次选择菜品购买
     *
     * @param order   订单id
     * @param session session
     * @return 结果
     */
    @PostMapping("/again")
    public R<String> againOrders(@RequestBody Orders order, HttpSession session) {
        return R.success("选择菜品，再来一单");
    }

    /**
     * @param page      分页显示page页
     * @param pageNum   每页显示的数量
     * @param number    订单号
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return page
     */
    //···?page=1&pageSize=10&number=123&beginTime=2022-10-03%2000%3A00%3A00&endTime=2022-10-20%2023%3A59%3A59
    @GetMapping("/page")
    public R<Page<OrderDto>> orderPage(@RequestParam(value = "page") Integer page,
                                       @RequestParam(value = "pageSize") Integer pageNum,
                                       @Nullable @RequestParam("number") String number,
                                       @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("beginTime") LocalDateTime beginTime,
                                       @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "endTime") LocalDateTime endTime) {
        log.info("后端获取订单明细的参数：page：{},pageSize：{},订单号：{}，开始时间：{},结束时间：{}", page, pageNum, number, beginTime, endTime);
        Page<Orders> objectPage = new Page<>(page, pageNum);
        Page<OrderDto> pageInfo = orderService.getPages(objectPage, number, beginTime, endTime);
        return R.success(pageInfo);
    }


    @PutMapping("")
    public R<String> updateStatus(@RequestBody Orders order) {
        log.info("修改订单状态传来的参数：order:{}", order);
        //id,status
        boolean flag = orderService.updateStatus(order);
        return flag ? R.success("修改成功") : R.error("修改失败");
    }

}
