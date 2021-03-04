package com.happy.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.server.common.RespBean;
import com.happy.server.mapper.DepartmentMapper;
import com.happy.server.pojo.Department;
import com.happy.server.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartment() {

        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    public RespBean addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if(1 == dep.getResult()){
            return RespBean.success("添加成功",dep);
        }
        return RespBean.error("添加失败");
    }


    @Override
    public RespBean delDep(Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.delDep(department);
        if(department.getResult() == -2){
            return RespBean.error("该部门下，还有子部门，删除失败");
        }else if(-1 == department.getResult()){
            return RespBean.error("该部门下还有员工删除失败");
        }else if(1 == department.getResult()){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
