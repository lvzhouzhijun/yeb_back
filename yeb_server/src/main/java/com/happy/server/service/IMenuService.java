package com.happy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户Id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();

}
