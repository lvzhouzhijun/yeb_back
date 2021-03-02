package com.happy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy.server.pojo.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenusByAdminId(@Param("id") Integer id);

    List<Menu> getMenusWithRole();
}
