package com.happy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy.server.pojo.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(@Param("parentId") Integer parentId);

    void addDep(Department dep);

    void delDep(Department department);

}
