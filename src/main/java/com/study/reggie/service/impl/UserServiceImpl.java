package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.entities.User;
import com.study.reggie.mapper.UserMapper;
import com.study.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author That's all
 * @description 针对表【user(用户信息)】的数据库操作Service实现
 * @createDate 2022-10-16 21:28:23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

}




