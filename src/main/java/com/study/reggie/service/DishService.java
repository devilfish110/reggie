package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.Dish;
import com.study.reggie.entities.dto.DishDto;
import com.study.reggie.exception.CustomerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author That's all
 * @description 针对表【dish(菜品管理)】的数据库操作Service
 * @createDate 2022-10-14 22:18:18
 */
@Transactional(rollbackFor = Exception.class)
public interface DishService extends IService<Dish> {

    /**
     * 分页查询菜品(dish)及所属分类(category)
     *
     * @param page 查询page前页
     * @param size 每页显示size条数据
     * @param name 菜名,可有可无,条件查询
     * @return Page<Dish>
     */
    Page<DishDto> getPage(int page, int size, String name);

    /**
     * 添加菜品
     *
     * @param dishDto 封装了菜品和口味的Vo
     * @return 添加结果
     * @throws CustomerException 自定义异常
     */
    boolean saveDishAndFlavor(DishDto dishDto) throws CustomerException;

    /**
     * 获取菜品信息及口味
     *
     * @param id 菜品ID
     * @return VO
     */
    DishDto getDishAndFlavors(Long id);

    /**
     * 修改菜品信息及口味
     *
     * @param dishDto 封装菜品信息及口味的VO
     * @return 修改结果
     */
    boolean updateDishAndFlavors(DishDto dishDto);

    /**
     * 删除菜品信息及口味
     *
     * @param id 菜品id
     * @return 删除结果
     */
    boolean removeDishAndFlavors(Long id);

    /**
     * 获取菜品列表
     *
     * @param id   分类ID
     * @param status 菜品售卖状态
     * @param name 菜品名称
     * @return DishDto的List
     */
    List<DishDto> getList(Long id,Integer status, String name);
}
