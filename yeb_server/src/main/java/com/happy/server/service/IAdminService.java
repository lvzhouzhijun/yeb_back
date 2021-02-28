package com.happy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.server.common.RespBean;
import com.happy.server.pojo.Admin;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录成功之后返回 token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password, HttpServletRequest request);
}
