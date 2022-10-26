package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.SetmealDish;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author That's all
 * @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Service
 * @createDate 2022-10-16 15:06:53
 */
@Transactional(rollbackFor = Exception.class)
public interface SetmealDishService extends IService<SetmealDish> {

}
