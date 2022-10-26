package com.study.reggie.controller;

import com.study.reggie.common.R;
import com.study.reggie.entities.ShoppingCart;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 查询用户的购物车列表
     *
     * @param session 获取用户ID
     * @return list
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getCartList(HttpSession session) {
        //获取userID
        Long userId = (Long) session.getAttribute("user");
        //查询当前用户的购物车信息
        List<ShoppingCart> list = shoppingCartService.getCartListByUserId(userId);
        return R.success(list);
    }

    /**
     * 移除商品
     *
     * @param cart    封装了dishId和setmealId的对象
     * @param session 获取用户id
     * @return 处理结果
     */
    @PostMapping("/sub")
    public R<String> removeCommodity(@RequestBody ShoppingCart cart, HttpSession session) throws CustomerException {
        if (cart != null) {
            //给购物车设置userId
            cart.setUserId((Long) session.getAttribute("user"));
            boolean flag = shoppingCartService.removeCommodity(cart);
            return flag ? R.success("移除成功") : R.error("移除失败");
        }
        return R.error("传入的参数为空");
    }

    /**
     * 添加商品
     *
     * @param cart    封装为shoppingCart的额信息
     * @param session 获取用户id
     * @return 添加结果
     */
    @PostMapping("/add")
    public R<String> addCommodity(@RequestBody ShoppingCart cart, HttpSession session) {
        log.info("添加购物车时传来的参数：cart:{}", cart);
        if (cart != null) {
            //获取用户id，设置是谁要添加商品
            Long userId = (Long) session.getAttribute("user");
            cart.setUserId(userId);
            boolean save = shoppingCartService.saveCommodity(cart);
            return save ? R.success("添加成功") : R.error("添加失败");
        }
        return R.error("传入的参数为空");
    }

    /**
     * 清空用户购物车信息
     *
     * @param session 获取用户id
     * @return 清空结果
     * @throws CustomerException 自定义异常
     */
    @DeleteMapping("/clean")
    public R<String> deleteCommodity(HttpSession session) throws CustomerException {
        Long userId = (Long) session.getAttribute("user");
        boolean flag = shoppingCartService.removeAllCommodityByUserId(userId);
        return flag ? R.success("清空成功") : R.error("未知错误");
    }

}
