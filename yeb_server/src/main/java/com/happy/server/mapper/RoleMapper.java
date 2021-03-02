package com.happy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy.server.pojo.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoles(@Param("adminId") Integer adminId);
}
