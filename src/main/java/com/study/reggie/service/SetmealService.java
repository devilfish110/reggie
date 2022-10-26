package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.Setmeal;
import com.study.reggie.entities.dto.SetmealDto;
import com.study.reggie.exception.CustomerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author That's all
 * @description 针对表【setmeal(套餐)】的数据库操作Service
 * @createDate 2022-10-16 14:35:31
 */

@Transactional(rollbackFor = Exception.class)
public interface SetmealService extends IService<Setmeal> {

    /**
     * 查询setmeal和setmealDish的VO分页(setmeal,setmealDish)
     *
     * @param page    查询page页
     * @param pageNum 每页显示pageNum条数据
     * @param name    套餐名
     * @return Page<SetmealDto>
     */
    Page<SetmealDto> getPage(Integer page, Integer pageNum, String name);

    /**
     * 保存套餐及套餐的菜品
     *
     * @param setmealDto 封装了套餐信息及套餐菜品的对象
     * @return 添加结果
     * @throws CustomerException 自定义异常
     */
    boolean saveSetmealAndDish(SetmealDto setmealDto) throws CustomerException;

    /**
     * 查询套餐及套餐的菜品
     *
     * @param id 套餐ID
     * @return 套餐及套餐的菜品Vo
     */
    SetmealDto getSetmealAndDishById(Long id);

    /**
     * 翠盖套餐及套餐的菜品
     *
     * @param setmealDto 套餐及套餐内的菜品新的数据
     * @return 修改结果
     */
    boolean updateSetmealAndDishById(SetmealDto setmealDto);

    /**
     * 修改setmeal的status
     *
     * @param ids    setmeal的id集合
     * @param status 新的status数据
     * @return 修改结果
     */
    boolean updateSetmealStatusByIds(List<Long> ids, Integer status);

    /**
     * 删除setmeal信息及相关的setmealDish信息
     *
     * @param ids setmealId
     * @return 删除结果
     */
    boolean removeSetmealAndSetmealDish(List<Long> ids);

    /**
     * 获取setmeal和setmealDish的组合List
     *
     * @param categoryId 分类id
     * @param status     状态码
     * @return list
     */
    List<SetmealDto> getSetmealAndDishList(Long categoryId, Integer status);
}
