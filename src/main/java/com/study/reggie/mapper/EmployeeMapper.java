package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【employee(员工信息)】的数据库操作Mapper
 * @createDate 2022-10-13 23:49:13
 * @Entity com.study.reggie.entities.Employee
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




