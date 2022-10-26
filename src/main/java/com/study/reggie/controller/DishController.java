package com.study.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;
import com.study.reggie.entities.Dish;
import com.study.reggie.entities.dto.DishDto;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取菜品分类分页
     *
     * @param page 查询page页
     * @param size 每页显示size条数据
     * @param name 菜名，可有可无(条件)
     */
    @GetMapping("page")
    public R<Page<DishDto>> page(@RequestParam("page") int page, @RequestParam("pageSize") int size, @Nullable @RequestParam("name") String name) {
        log.info("菜品管理:Page,传来的参数：{},{},{}", page, size, name);
        Page<DishDto> pageInfo = dishService.getPage(page, size, name);
        return R.success(pageInfo);
    }

    /**
     * 添加菜品及菜品口味
     * 插入一条dish时把新数据添加到redis缓存中
     *
     * @param dishDto 封装了菜品的信息及菜品口味
     */
    @PostMapping()
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> save(@RequestBody DishDto dishDto) throws CustomerException {
        //打印时不会显示dish相关的内容,debug可以看到信息正确
        log.info("添加菜品时传的参数：{}", dishDto);
        //dishName,flavors
        boolean save = dishService.saveDishAndFlavor(dishDto);
        return save ? R.success("添加成功") : R.error("添加失败");
    }

    /**
     * 查询菜品信息及菜品口味(VO)
     *
     * @param id 菜品ID
     * @return 修改结果信息
     */
    @GetMapping("/{id}")
    @Cacheable(value = "dishCache", key = "'dish_'+#id")
    public R<DishDto> edit(@PathVariable("id") Long id) {
        log.info("编辑菜品时菜品的信息id：{}", id);
        //编辑菜品时菜品的信息id：1581247884211261441
        //dish表中的id对应dishFlavor表中的dishId
        DishDto dishDto = dishService.getDishAndFlavors(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品信息及口味
     * dish,dishFlavor
     *
     * @param dishDto 封装菜品信息及口味的VO
     * @return 修改结果信息
     * @CacheEvict 修改数据后清除redis缓存
     */
    @PutMapping()
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> update(@RequestBody DishDto dishDto) {
        //打印时不会显示dish相关的内容,debug可以看到信息正确
        log.info("添加菜品时传的参数：{}", dishDto);
        boolean update = dishService.updateDishAndFlavors(dishDto);
        return update ? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 修改菜品售卖状态(status)
     * url:···/status/0?ids=1581247884211261441,1413384757047271425
     *
     * @param status 售卖状态
     * @param ids    菜品id的集合
     * @return 修改结果信息
     * @CacheEvict 修改数据后清除redis缓存
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> updateDishStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        log.info("传递的状态：{}，IDs：{}", status, ids);
        //传递的状态：0，IDs：[1581247884211261441, 1413384757047271425]
        //查询ids对应的list结果
        List<Dish> dishList = dishService.listByIds(ids);
        for (Dish dish : dishList) {
            //设置所有的dish的status值为传进来的status
            dish.setStatus(status);
        }
        //批量修改
        boolean update = dishService.updateBatchById(dishList);
        return update ? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 根据ID删除菜品信息及菜品口味(dish,dishFlavors)
     * url:···?ids=1581247884211261441,1413384757047271425
     *
     * @param ids 菜品的ID列表
     * @return 删除结果
     * @CacheEvict 删除一条dish时清除redis的数据
     */
    @DeleteMapping()
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> deleteDish(@RequestParam("ids") List<Long> ids) {
        log.info("删除菜品及口味时传递的id：{}", ids);
        //删除菜品及口味时传递的参数：[1581247884211261441, 1413384757047271425]
        //进入循坏开始逐个修改
        for (Long id : ids) {
            boolean remove = dishService.removeDishAndFlavors(id);
            //判断是否删除成功
            if (!remove) {
                //出现一个false,事务回滚,删除失败
                return R.error("删除失败");
            }
        }
        return R.success("删除成功");
    }


    /**
     * 获取菜品列表
     *
     * @param dish 封装了前端传来的：分类ID，菜品售卖状态，菜品名称
     * @Cacheable 查询数据前获取redis的缓存数据
     */
    @GetMapping("/list")
    @Cacheable(value = "dishCache", key = "'dish_'+#dish.categoryId+'_'+#dish.status+'_'+#dish.name")
    public R<List<DishDto>> getDishList(Dish dish) {
        String dishName = dish.getName();
        Integer status = dish.getStatus();
        Long categoryId = dish.getCategoryId();
        log.info("查询菜品List传进来的参数id：{},status:{},name:{}", categoryId, status, dishName);
        List<DishDto> list = dishService.getList(categoryId, status, dishName);
        return R.success(list);
    }
}
