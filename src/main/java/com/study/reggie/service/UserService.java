package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author That's all
 * @description 针对表【user(用户信息)】的数据库操作Service
 * @createDate 2022-10-16 21:28:23
 */
@Transactional(rollbackFor = Exception.class)
public interface UserService extends IService<User> {

}
