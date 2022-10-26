package com.study.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;
import com.study.reggie.entities.Employee;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

/**
 * 员工信息 前端控制器
 *
 * @author That's all
 * @since 2022-10-13
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;


    /**
     * 员工登录
     *
     * @param employee 员工对象
     * @param session  存储登陆结果
     * @return 返回R到前端
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session) {
        log.info("收到的emp参数:{}", employee);
        Employee emp = employeeService.getLogin(employee);
        //判断是否存在员工
        if (emp != null) {
            //传进来的密码MD5加密
            String userPassword = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes(StandardCharsets.UTF_8));
            //密码比对
            if (emp.getPassword().equals(userPassword)) {
                //判断员工状态
                if (emp.getStatus() != 0) {
                    //登陆成功，存入session
                    session.setAttribute("employee", emp.getId());
                    emp.setPassword("");
                    emp.setIdNumber("");
                    return R.success(emp);
                } else {
                    return R.error("账号已禁用");
                }
            } else {
                return R.error("密码错误");
            }
        }
        return R.error("账号不存在");
    }

    /**
     * 员工分页查询
     *
     * @param page 当前显示的页面(int)
     * @param size 每一页显示的数据条数
     * @param name 员工姓名,可选查询
     * @return 封装了员工信息列表的R对象
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(@RequestParam("page") int page, @RequestParam("pageSize") int size, @Nullable @RequestParam("name") String name) {
        Page<Employee> pageInfo = employeeService.getPage(page, size, name);
        return R.success(pageInfo);
    }

    /**
     * 添加员工
     *
     * @param employee 新的员工对象
     * @return R对象
     */
    @PostMapping()
    @CacheEvict(value = "empCache", allEntries = true)
    public R<String> addEmployee(@RequestBody Employee employee) throws CustomerException {
        log.info("收到的emp对象：{}", employee);
        boolean flag = employeeService.saveEmployee(employee);
        return flag ? R.success("添加成功") : R.error("添加失败");
    }

    /**
     * 根据员工id查询员工信息
     *
     * @param id 员工id
     * @return 封装员工的R对象
     */
    @GetMapping("/{id}")
    @Cacheable(value = "empCache", key = "'emp_'+#id")
    public R<Employee> getEmployeeById(@PathVariable("id") Long id) {
        log.info("收到的employee的ID：{}", id);
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("未查询到员工的信息");
    }

    /**
     * 修改员工信息
     *
     * @param employee 新的员工信息
     * @return 封装操作结果的R对象
     */
    @PutMapping()
    @CacheEvict(value = "empCache", allEntries = true)
    public R<String> updateEmployee(@RequestBody Employee employee) {
        log.info("传进来的修改对象：{}", employee);
        boolean update = employeeService.updateById(employee);
        if (update) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }


    /**
     * 员工退出登录
     *
     * @param session 删除session中的记录
     * @return R对象
     */
    @PostMapping("/logout")
    @CacheEvict(value = "empCache", allEntries = true)
    public R<String> logout(HttpSession session) {
        session.removeAttribute("employee");
        return R.success("成功退出");
    }

}
