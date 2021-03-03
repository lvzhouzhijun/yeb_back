package com.happy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.server.common.RespBean;
import com.happy.server.pojo.MenuRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
