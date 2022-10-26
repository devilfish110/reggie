package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.common.ObjectConversionObject;
import com.study.reggie.entities.*;
import com.study.reggie.entities.dto.OrderDto;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.mapper.OrdersMapper;
import com.study.reggie.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author That's all
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2022-10-18 02:36:43
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    @Autowired
    private AddressBookService addressService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Override
    public boolean submit(Orders orders) throws CustomerException {
        if (orders != null) {
            //地址表根据userId获取其他信息
            /*
            用户名：
            性别：
            手机号：
            地址:province_name + city_name + district_name + detail
            */
            //获取收货人信息地址
            AddressBook addressBook = addressService.getDefaultAddressByUserId(orders.getUserId());
            if (addressBook != null) {
                String name = addressBook.getConsignee();
                String phone = addressBook.getPhone();
                String address = "";
                String province = addressBook.getProvinceName();
                String city = addressBook.getCityName();
                String district = addressBook.getDistrictName();
                String detail = addressBook.getDetail();
                if (StringUtils.isNotBlank(province)) {
                    address += province;
                }
                if (StringUtils.isNotBlank(city)) {
                    address += city;
                }
                if (StringUtils.isNotBlank(district)) {
                    address += district;
                }
                if (StringUtils.isNotBlank(detail)) {
                    address += detail;
                }
                //购物车信息查询
                List<ShoppingCart> carts = shoppingCartService.getCartListByUserId(orders.getUserId());
                if (!carts.isEmpty()) {
                    //生成订单号
                    Long orderId = IdWorker.getId();
                    //计算待支付的总金额
                    AtomicInteger amounts = new AtomicInteger(0);
                    //每个商品的金额
                    AtomicInteger oneAmount = new AtomicInteger(0);
                    //订单明细表(多条数据)
                    /*
                    订单号：order_id
                    收货人：name
                    菜品，套餐图片：image
                    菜品号：dish_id
                    口味：dish_flavor
                    套餐号：setmeal_id
                    数量：number
                    金额:amount
                     */
                    List<OrderDetail> list = new ArrayList<>();
                    carts.forEach(shoppingCart -> {
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setOrderId(orderId);
                        orderDetail.setName(shoppingCart.getName());
                        orderDetail.setImage(shoppingCart.getImage());
                        orderDetail.setDishId(shoppingCart.getDishId());
                        orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
                        orderDetail.setSetmealId(shoppingCart.getSetmealId());
                        orderDetail.setNumber(shoppingCart.getNumber());
                        //获取商品价格
                        BigDecimal amount = shoppingCart.getAmount();
                        //获取商品数量
                        Integer number = shoppingCart.getNumber();
                        int i = oneAmount.addAndGet(amount.multiply(new BigDecimal(number)).intValue());
                        //设置单份商品的总金额
                        orderDetail.setAmount(BigDecimal.valueOf(i));
                        oneAmount.set(0);
                        //设置待支付的总金额
                        amounts.getAndAdd(i);
                        list.add(orderDetail);
                    });
                    boolean saveOrderTail = orderDetailService.saveBatch(list);
                    if (saveOrderTail) {
                        //订单表(单条数据)
                            /*
                            订单号：number
                            订单状态: status
                            用户id:user_id session (已有)
                            地址簿id:addressBook_id (已有)
                            下单时间：order_time
                            结账时间:checkout_time
                            支付方式:pay_method (已有)
                            总金额:amount
                            备注:remark (已有)
                            手机号：phone 地址簿查询的
                            地址:address 地址簿查询的
                            下单人姓名:user_name
                            收货人姓名:consignee
                            */
                        orders.setNumber(String.valueOf(orderId));
                        //设置订单状态：
                        orders.setStatus(2);
                        //下单时间
                        orders.setOrderTime(LocalDateTime.now());
                        //结账时间
                        orders.setCheckoutTime(LocalDateTime.now());
                        //总支付金额(配送费也加进去)
                        orders.setAmount(BigDecimal.valueOf(amounts.addAndGet(6)));
                        //收货人手机号
                        orders.setPhone(phone);
                        //收货人地址
                        orders.setAddress(address);
                        //下单人姓名
                        orders.setUserName(userService.getById(orders.getUserId()).getName());
                        //收货人姓名
                        orders.setConsignee(name);
                        //保存信息
                        boolean saveOrders = super.save(orders);
                        if (saveOrders) {
                            //购物车清空
                            return shoppingCartService.removeAllCommodityByUserId(orders.getUserId());
                        } else {
                            //保存订单信息失败
                            return false;
                        }
                    } else {
                        //生成订单明细失败
                        return false;
                    }
                }
            } else {
                throw new CustomerException("收货地址为空，请设置收货地址");
            }
        } else {
            throw new CustomerException("错误的请求");
        }
        return false;
    }


    @Override
    public Page<OrderDto> getPageById(Page<Orders> page, Long id) throws CustomerException {
        if (id != null && page != null) {
            //查询orders表
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Orders::getUserId, id)
                    .orderByDesc(Orders::getOrderTime);
            Page<Orders> ordersPage = super.page(page, queryWrapper);
            //创建orderDto的Page
            Page<OrderDto> orderDtoPage = new Page<>();
            Page<OrderDto> orderVoPage = ObjectConversionObject.convertPageVo(ordersPage, orderDtoPage);
            //创建Dtolist
            ArrayList<OrderDto> list = new ArrayList<>();
            List<Orders> orderList = ordersPage.getRecords();
            orderList.forEach(order -> {
                OrderDto orderDto = ObjectConversionObject.convertOrderVo(order);
                //查询orderDetails
                //order表中的number对应orderDetail表中的order_id
                String orderId = order.getNumber();
                List<OrderDetail> orderDetailList = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, orderId));
                //设置缺少的属性orderDetails
                orderDto.setOrderDetails(orderDetailList);
                list.add(orderDto);
            });
            //设置Page的records属性
            orderVoPage.setRecords(list);
            return orderVoPage;
        } else {
            throw new CustomerException("未登录!");
        }
    }

    @Override
    public boolean againOrders(Orders order) {
        if (order != null) {
            //只有这两个信息
            Long orderId = order.getId();
            Long userId = order.getUserId();
            Orders orders = super.getById(orderId);
            //获取在orderDetail中的order_id
            String number = orders.getNumber();
            //查询orderDetail的数据
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, number);
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
            //复制orderDetail数据
            long id = IdWorker.getId();
            orderDetails.forEach(orderDetail -> {
                //设置新的订单号(于orders表的number一致)
                orderDetail.setOrderId(id);
                //设置新的主键
                orderDetail.setId(IdWorker.getId());
            });
            //添加新数据（orderDetails）
            boolean saveOrderTails = orderDetailService.saveBatch(orderDetails);
            //设置新的订单号number
            orders.setNumber(String.valueOf(id));
            //设置新的主键
            orders.setId(IdWorker.getId());
            //设置新的订单状态
            orders.setStatus(1);
            //设置新的下单时间
            orders.setOrderTime(LocalDateTime.now());
            orders.setCheckoutTime(LocalDateTime.now());
            orders.setRemark("");
            boolean saveOrder = super.save(orders);
            return saveOrderTails && saveOrder;
        }
        return false;
    }


    @Override
    public Page<OrderDto> getPages(Page<Orders> page, String number, LocalDateTime beginTime, LocalDateTime endTime) {
        if (page != null) {
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotBlank(number), Orders::getNumber, number)
                    .ge(beginTime != null, Orders::getOrderTime, beginTime)
                    .le(endTime != null, Orders::getCheckoutTime, endTime);
            Page<Orders> ordersPage = super.page(page, queryWrapper);
            //复制ordersPage数据为OrderDtoPage，除了records
            Page<OrderDto> dtoPage = ObjectConversionObject.convertPageVo(ordersPage, new Page<OrderDto>());
            //处理records数据
            List<Orders> records = ordersPage.getRecords();
            //创建保存orderDto的列表
            ArrayList<OrderDto> orderDtos = new ArrayList<>();
            records.forEach(item -> {
                Long userId = item.getUserId();
                User user = userService.getById(userId);
                OrderDto orderDto = ObjectConversionObject.convertOrderVo(item);
                //补全orderDto的orderDetails
                List<OrderDetail> orderDetails = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, item.getNumber()));
                orderDto.setOrderDetails(orderDetails);
                //补全orderDto的name属性
                orderDto.setName(user.getName());
                //把数据拿出来保存到外面
                orderDtos.add(orderDto);
            });
            //赋值
            dtoPage.setRecords(orderDtos);
            return dtoPage;
        }
        return null;
    }


    @Override
    public boolean updateStatus(Orders order) {
        if (order != null) {
            LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Orders::getId, order.getId())
                    .set(Orders::getStatus, order.getStatus());
            return super.update(updateWrapper);
        }
        return false;
    }
}




