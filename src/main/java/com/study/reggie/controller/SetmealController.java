package com.study.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;
import com.study.reggie.entities.dto.SetmealDto;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 查询套餐信息(setmeal,setmealDish)
     * url:···/page?page=1&pageSize=10&name=儿童套餐
     *
     * @param page    查询page页
     * @param pageNum 每页显示pageNum条数据
     * @param name    套餐名
     * @return Page<SetmealDto>
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer page,
                                    @RequestParam("pageSize") Integer pageNum,
                                    @Nullable @RequestParam("name") String name) {
        log.info("套餐管理:Page,传来的参数：{},{},{}", page, pageNum, name);
        Page<SetmealDto> setmealDtoPage = setmealService.getPage(page, pageNum, name);
        return R.success(setmealDtoPage);
    }


    /**
     * 新建套餐
     *
     * @param setmealDto 封装了套餐信息及套餐菜品的对象
     * @return 添加结果
     */
    @PostMapping()
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto) throws CustomerException {
        log.info("套餐管理的添加套餐传入的参数:{}", setmealDto);
        boolean flag = setmealService.saveSetmealAndDish(setmealDto);
        return flag ? R.success("添加成功") : R.error("添加失败");
    }

    /**
     * 编辑套餐时查询套餐信息
     *
     * @param id 套餐ID
     * @return 查询到的结果
     */
    @GetMapping("/{id}")
    @Cacheable(value = "setmealCache",key = "'setmeal_'+#id")
    public R<SetmealDto> getSetmealById(@PathVariable("id") Long id) {
        log.info("修改套餐信息时查询的传入的参数setmealId:{}", id);
        SetmealDto setmealDto = setmealService.getSetmealAndDishById(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDto
     * @return
     */
    @PutMapping()
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        log.info("修改套餐时传进来新的套餐信息参数：{}", setmealDto);
        boolean updateSetmeal = setmealService.updateSetmealAndDishById(setmealDto);
        return updateSetmeal ? R.success("修改成功") : R.error("修改成失败");
    }


    /**
     * 修改套餐的售卖状态
     * url:···/status/0?ids=1581610898282311681,1415580119015145474
     *
     * @param status 新的状态码
     * @param ids    套餐id集合
     * @return 修改结果
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> updateSetmealStatus(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> ids) {
        log.info("修改状态时传入的参数status：{},ids:{}", status, ids);
        boolean updateStatus = setmealService.updateSetmealStatusByIds(ids, status);
        return updateStatus ? R.success("修改状态成功") : R.error("修改状态失败");
    }

    /**
     * 删除套餐
     *
     * @param ids 套餐的id集合
     * @return 删除结果
     */
    @DeleteMapping("")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> deleteSetmeal(@RequestParam("ids") List<Long> ids) {
        log.info("删除套餐时传来的信息ids：{}", ids);
        boolean deleteSetmeal = setmealService.removeSetmealAndSetmealDish(ids);
        return deleteSetmeal ? R.success("删除成功") : R.error("删除失败");
    }

    //···/setmeal/list?categoryId=1413342269393674242&status=1
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "'setmeal_'+#categoryId+'_'+#status")
    public R<List<SetmealDto>> getList(@RequestParam("categoryId") Long categoryId, @RequestParam("status") Integer status) {
        log.info("获取套餐信息列表传递的参数：categoryId:{}，status:{}", categoryId, status);
        List<SetmealDto> setmealDtoList = setmealService.getSetmealAndDishList(categoryId, status);
        return R.success(setmealDtoList);
    }

}
