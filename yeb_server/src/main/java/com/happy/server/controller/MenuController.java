package com.happy.server.controller;


import com.happy.server.pojo.Menu;
import com.happy.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "通过用户Id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        return adminService.getMenusByAdminId();
    }
}
