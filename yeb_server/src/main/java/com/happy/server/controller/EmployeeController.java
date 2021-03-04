package com.happy.server.controller;


import com.happy.server.common.RespPageBean;
import com.happy.server.pojo.Employee;
import com.happy.server.service.IEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "获取所有员工分页数据")
    @GetMapping("/")
    public RespPageBean getEmployee(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10")Integer size,
            Employee employee, LocalDate [] beginDateScope){
        return employeeService.getEmployeeByPage(currentPage,size,employee,beginDateScope);

    }

}
