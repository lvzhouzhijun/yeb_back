package com.happy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy.server.pojo.Admin;
import com.happy.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface AdminMapper extends BaseMapper<Admin> {

    List<Menu> getMenusByAdminId(Integer id);
}
