package com.happy.server.controller;

import com.happy.server.common.RespBean;
import com.happy.server.pojo.AdminLoginParam;
import com.happy.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/02/28/19:34
 * @Description:
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录时候返回token")
    @PostMapping("/login")
    public RespBean login(AdminLoginParam adminLoginParam){
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),request);
    }

}
