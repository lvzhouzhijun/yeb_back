package com.happy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.server.common.RespBean;
import com.happy.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartment();

    /**
     * 添加部门
     * @param dep
     * @return
     */
    RespBean addDep(Department dep);
}
