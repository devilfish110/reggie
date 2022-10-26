package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.entities.Employee;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.mapper.EmployeeMapper;
import com.study.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author That's all
 * 针对表【employee(员工信息)】的数据库操作Service实现
 * 2022-10-13 23:49:13
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {


    @Override
    public Employee getLogin(Employee employee) {
        //获取用户名
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee one = this.getOne(queryWrapper);
        queryWrapper.clear();
        return one;
    }


    @Override
    public Page<Employee> getPage(int page, int size, String name) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(name), Employee::getName, name);
        Page<Employee> pageInfo = this.page(new Page<>(page, size), queryWrapper);
        queryWrapper.clear();
        return pageInfo;
    }

    @Override
    public boolean saveEmployee(Employee employee) throws CustomerException {
        //数据库中username字段存在唯一约束(登录账号)
        //先查询有没有这个账号
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        long count = super.count(queryWrapper);
        queryWrapper.clear();
        if (!Objects.equals(count, 0L)) {
            //存在该用户,直接抛出异常
            throw new CustomerException("员工账号：" + username + "已存在");
        } else {
            //设置初始化密码123456,因为数据库中新员工的状态码(status)存在默认值：1
            String defaultPassword = "123456";
            //MD5加密
            String md5Password = DigestUtils.md5DigestAsHex(defaultPassword.getBytes(StandardCharsets.UTF_8));
            employee.setPassword(md5Password);
            return this.save(employee);
        }
    }


}




