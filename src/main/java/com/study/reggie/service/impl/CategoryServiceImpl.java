package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.entities.Category;
import com.study.reggie.entities.Dish;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.mapper.CategoryMapper;
import com.study.reggie.service.CategoryService;
import com.study.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author That's all
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2022-10-14 18:21:33
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Override
    public Page<Category> getPage(int page, int size) {
        //按照排序升序,再按照修改时间升序
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        return super.page(new Page<>(page, size), queryWrapper);
    }

    /**
     * 根据categoryId删除category信息及dish中绑定的信息
     *
     * @param id 分类id
     * @return 是否移除
     */
    @Override
    public boolean removeById(Serializable id) {
        //1.删除category表信息
        boolean removeCategory = super.removeById(id);
        //2.删除与category表关联的dish表信息(dish表中存在category_id绑定category表中的id)
        if (removeCategory) {
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getCategoryId, id);
            dishService.remove(queryWrapper);
        }
        return removeCategory;
    }

    @Override
    public List<Category> getList(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType())
                .orderByDesc(Category::getUpdateTime);
        return super.list(queryWrapper);
    }


    @Override
    public boolean saveCategory(Category category) throws CustomerException {
        if (category != null) {
            //分类名称存在唯一约束
            String categoryName = category.getName();
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getName, categoryName);
            long count = super.count(queryWrapper);
            queryWrapper.clear();
            if (!Objects.equals(count, 0L)) {
                //存在信息，抛出异常
                throw new CustomerException("分类名称：" + categoryName + "，已存在");
            } else {
                //不存在唯一约束冲突，直接添加
                return super.save(category);
            }
        }
        return false;
    }
}




