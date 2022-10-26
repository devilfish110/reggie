package com.study.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;
import com.study.reggie.entities.Category;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分类查询  分页
     *
     * @param page 显示page页
     * @param size 每页显示的数据条数
     * @return 封装了Category的Page
     */
    @GetMapping("page")
    public R<Page<Category>> page(@RequestParam("page") int page, @RequestParam("pageSize") int size) {
        log.info("传入的参数：{},显示的数量：{}", page, size);
        Page<Category> pageInfo = categoryService.getPage(page, size);
        return R.success(pageInfo);
    }

    /**
     * 添加分类
     *
     * @param category 分类信息
     * @return R对象
     */
    @PostMapping()
    @CacheEvict(value = "categoryCache", allEntries = true)
    public R<String> save(@RequestBody Category category) throws CustomerException {
        log.info("保存Category时传入的参数：{}", category);
        boolean save = categoryService.saveCategory(category);
        return save ? R.success("添加成功") : R.error("添加失败");
    }

    /**
     * 修改分类信息
     *
     * @param category 新的分类信息实体
     * @return R对象
     */
    @PutMapping()
    @CacheEvict(value = "categoryCache", allEntries = true)
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类传来的参数：{}", category);
        if (category != null) {
            boolean update = categoryService.updateById(category);
            return update ? R.success("修改成功") : R.error("修改失败");
        }
        return null;
    }

    /**
     * 删除分类
     * 请求url：···?id=1580900368009715713
     *
     * @param categoryId 分类ID
     * @return R<String>
     */
    @DeleteMapping()
    @CacheEvict(value = "categoryCache", allEntries = true)
    public R<String> delete(@RequestParam("id") Long categoryId) {
        boolean remove = categoryService.removeById(categoryId);
        return remove ? R.success("删除成功") : R.error("删除失败");
    }

    /**
     * url上携带参数:···?type=1
     * category中存在type属性，可以用实体类接收
     *
     * @param category 分类信息
     * @return R
     */
    @GetMapping("/list")
    @Cacheable(value="categoryCache",key = "'category_'+#category.type")
    public R<List<Category>> list(Category category) {
        log.info("添加菜品时调用方法传过来的参数：{}", category);
        List<Category> list = categoryService.getList(category);
        return R.success(list);
    }


}
