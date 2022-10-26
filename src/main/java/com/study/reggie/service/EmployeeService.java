package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.Employee;
import com.study.reggie.exception.CustomerException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author That's all
 * 针对表【employee(员工信息)】的数据库操作Service
 * 2022-10-13 23:49:13
 */
@Transactional(rollbackFor = Exception.class)
public interface EmployeeService extends IService<Employee> {

    /**
     * 根据员工的用户名查询用户
     *
     * @param employee 用户名和密码封装的实体类对象
     * @return 查询到的结果对象
     */
    Employee getLogin(Employee employee);

    /**
     * 查询员工分页
     *
     * @param page 显示page页的数据
     * @param size 每一页显示多少条数据
     * @param name 员工姓名
     * @return 封装了Employee的page对象
     */
    Page<Employee> getPage(int page, int size, String name);

    /**
     * 保存新员工
     *
     * @param employee 新员工信息实体类
     * @return 保存结果flag
     * @throws CustomerException 自定义异常
     */
    boolean saveEmployee(Employee employee) throws CustomerException;
}
