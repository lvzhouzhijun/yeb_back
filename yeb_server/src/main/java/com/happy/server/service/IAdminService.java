package com.happy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.server.common.RespBean;
import com.happy.server.pojo.Admin;
import com.happy.server.pojo.Menu;
import com.happy.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录成功之后返回 token
     *
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据用户Id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRole(Integer adminId);

}
