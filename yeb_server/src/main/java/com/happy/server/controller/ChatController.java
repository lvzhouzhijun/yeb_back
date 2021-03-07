package com.happy.server.controller;

import com.happy.server.pojo.Admin;
import com.happy.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/07/13:59
 * @Description: 在线聊天
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取所有的操作员")
    @GetMapping("/admin")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }
}
