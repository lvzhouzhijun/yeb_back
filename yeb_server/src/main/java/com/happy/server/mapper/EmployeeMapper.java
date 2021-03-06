package com.happy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.server.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 获取所有员工分页
     * @param page
     * @param employee
     * @param beginDateScope
     * @return
     */
    IPage<Employee> getEmployeeByPage(Page<Employee> page,
                                      @Param("employee") Employee employee,
                                      @Param("beginDateScope") LocalDate[] beginDateScope);

    /**
     * 查询员工，如果Id为空，就查所有员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(@Param("id") Integer id);

    /**
     * 获取所有员工的账套
     * @param page
     * @return
     */
    IPage<Employee> getEmployeeWithSalary(Page<Employee> page);
}
