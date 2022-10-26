package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
 * @createDate 2022-10-14 18:21:33
 * @Entity com.study.reggie.entities.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




