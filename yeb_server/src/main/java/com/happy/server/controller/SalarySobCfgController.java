package com.happy.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.happy.server.common.RespBean;
import com.happy.server.common.RespPageBean;
import com.happy.server.pojo.Employee;
import com.happy.server.pojo.Salary;
import com.happy.server.service.IEmployeeService;
import com.happy.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/06/22:13
 * @Description:
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;

    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/salaries")
    public List<Salary> getAllSalary() {
        return salaryService.list();
    }

    @ApiOperation(value = "获取所有员工的账套")
    @GetMapping("/")
    public RespPageBean getEmployeeWithSalary(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return employeeService.getEmployeeWithSalary(currentPage,size);
    }

    @ApiOperation(value = "更新员工账套")
    @PutMapping("/")
    public RespBean updateEmpSalary(Integer eid,Integer sid){
        if(employeeService.update(new UpdateWrapper<Employee>()
            .set("salaryId",sid).eq("id",eid))){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

}
