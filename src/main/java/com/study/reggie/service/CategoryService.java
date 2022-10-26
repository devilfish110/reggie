package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.Category;
import com.study.reggie.exception.CustomerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author That's all
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service
 * @createDate 2022-10-14 18:21:33
 */
@Transactional(rollbackFor = Exception.class)
public interface CategoryService extends IService<Category> {

    /**
     * 查询分类分页
     *
     * @param page 显示page页的数据
     * @param size 每一页显示多少条数据
     * @return 封装了Category的page对象
     */
    Page<Category> getPage(int page, int size);

    /**
     * 查询分类返回列表(根据type)
     *
     * @param category 封装了查询条件的实体类
     * @return list
     */
    List<Category> getList(Category category);

    /**
     * 添加分类信息
     *
     * @param category 分类信息
     * @return 添加结果
     * @throws CustomerException 自定义异常
     */
    boolean saveCategory(Category category) throws CustomerException;
}
