package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【user(用户信息)】的数据库操作Mapper
 * @createDate 2022-10-16 21:28:23
 * @Entity com.study.reggie.entities.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




