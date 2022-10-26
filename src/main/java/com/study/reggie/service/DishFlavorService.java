package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.DishFlavor;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author That's all
 * @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service
 * @createDate 2022-10-15 13:46:41
 */
@Transactional(rollbackFor = Exception.class)
public interface DishFlavorService extends IService<DishFlavor> {

}
