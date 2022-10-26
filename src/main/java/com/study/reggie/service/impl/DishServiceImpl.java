package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.common.ObjectConversionObject;
import com.study.reggie.entities.Category;
import com.study.reggie.entities.Dish;
import com.study.reggie.entities.DishFlavor;
import com.study.reggie.entities.dto.DishDto;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.mapper.CategoryMapper;
import com.study.reggie.mapper.DishMapper;
import com.study.reggie.service.DishFlavorService;
import com.study.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author That's all
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2022-10-14 22:18:18
 */
@Slf4j
@Lazy
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public Page<DishDto> getPage(int page, int size, String name) {
        //1.获取dishPage
        Page<Dish> dishPage = new Page<>(page, size);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Dish::getName, name).orderByDesc(Dish::getUpdateTime);
        //得到有结果的dishPage
        this.page(dishPage, queryWrapper);
        queryWrapper.clear();
        //2.获取dishDtoPage
        /*把dishPage的值复制过来*/
        Page<DishDto> voPage = ObjectConversionObject.convertPageVo(dishPage, new Page<DishDto>());
        //2.1获取List<dishDto>
        //创建Vo的List
        ArrayList<DishDto> list = new ArrayList<>();
        //对dishDto赋值
        List<Dish> dishList = dishPage.getRecords();
        for (Dish dish : dishList) {
            if (dish != null) {
                Category category = categoryMapper.selectById(dish.getCategoryId());
                DishDto vo = ObjectConversionObject.convertDishVo(dish);
                vo.setCategoryName(category.getName());
                //dishDto添加到lList<dishDto>中
                list.add(vo);
            } else {
                return null;
            }
        }
        //给dishDtoPage的records赋值
        //records 用来存放查询出来的数据
        if (!list.isEmpty()) {
            voPage.setRecords(list);
        } else {
            voPage.setRecords(null);
        }
        return voPage;
    }

    @Override
    public boolean saveDishAndFlavor(DishDto dishDto) throws CustomerException {
        if (dishDto != null) {
            //菜品名称存在唯一约束
            String dishName = dishDto.getName();
            //查询看有没有
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getName, dishName);
            Long count = super.count(queryWrapper);
            queryWrapper.clear();
            if (Objects.equals(count, 0L)) {
                //1.保存菜品(dish表)信息
                boolean saveDish = super.save(dishDto);
                //2.保存dishFlavor表信息
                List<DishFlavor> flavors = dishDto.getFlavors();
                for (DishFlavor flavor : flavors) {
                    flavor.setDishId(dishDto.getId());
                }
                boolean saveFlavors = dishFlavorService.saveBatch(flavors);
                return saveDish && saveFlavors;
            } else {
                throw new CustomerException("菜名：" + dishName + "，已存在");
            }
        }
        return false;
    }

    @Override
    public DishDto getDishAndFlavors(Long id) {
        //dish表中的id对应dishFlavor表中的dishId
        //1.查询dish表信息
        Dish dish = super.getById(id);
        //把dish的数据赋值到dishDto中
        DishDto dishDto = ObjectConversionObject.convertDishVo(dish);
        //缺少dto中单独的属性List<DishFlavor>
        //2.获取DishFlavor
        //2.1获取dishId
        Long dishId = dishDto.getId();
        //由dishId查询dishFlavor表中相关信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId).orderByAsc(DishFlavor::getUpdateTime);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        //数据回填
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    @Override
    public boolean updateDishAndFlavors(DishDto dishDto) {
        //定义修改结果
        boolean saveFlavors = true;
        boolean updateDish = false;
        //修改dishFlavor表信息,update时不能确定主键，所以只能删除
        //获取DishFlavor,dishDto中缺少信息！
        //dishDto表中的id对应dishFlavor表中的dishId
        Long dishId = dishDto.getId();
        //通过dishId删除dishFlavor表中的相关数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        if (!dishFlavors.isEmpty()) {
            //表中存在相关数据,执行删除
            boolean removeFlavors = dishFlavorService.removeByIds(dishFlavors);
            if (!removeFlavors) {
                //删除失败,不执行任何添加，修改操作
                return false;
            }
        }
        //给dishFlavor确定dishID
        List<DishFlavor> flavors = dishDto.getFlavors();
        //部分菜品不用设置口味
        if (!flavors.isEmpty()) {
            //设置了口味
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            //开始添加dishFlavors数据
            saveFlavors = dishFlavorService.saveBatch(flavors);
        }
        //修改dish表信息
        updateDish = super.updateById(dishDto);
        return updateDish & saveFlavors;
    }

    @Override
    public boolean removeDishAndFlavors(Long id) {
        //删除dish表中的数据
        boolean removeDish = super.removeById(id);
        //删除dishFlavor表中的数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        boolean removeFlavors = dishFlavorService.remove(queryWrapper);
        return removeDish & removeFlavors;
    }

    @Override
    public List<DishDto> getList(Long id,Integer status, String name) {
        //获取dish的List
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(id != null, Dish::getCategoryId, id)
                .eq(status != null, Dish::getStatus, status)
                .like(StringUtils.isNotEmpty(name), Dish::getName, name).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = super.list(dishQueryWrapper);
        //转为dishDto集合
        ArrayList<DishDto> dishDtolist = new ArrayList<>();
        for (Dish dish : dishList) {
            if (dish != null) {
                DishDto dishDto = ObjectConversionObject.convertDishVo(dish);
                //dishDto中缺少DishFlavors
                LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
                dishFlavorQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId()).orderByDesc(DishFlavor::getUpdateTime);
                List<DishFlavor> flavors = dishFlavorService.list(dishFlavorQueryWrapper);
                //补全flavors
                dishDto.setFlavors(flavors);
                dishDtolist.add(dishDto);
            } else {
                return null;
            }
        }
        return dishDtolist;
    }
}




