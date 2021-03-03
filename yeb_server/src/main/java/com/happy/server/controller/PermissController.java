package com.happy.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.server.common.RespBean;
import com.happy.server.pojo.Menu;
import com.happy.server.pojo.MenuRole;
import com.happy.server.pojo.Role;
import com.happy.server.service.IMenuRoleService;
import com.happy.server.service.IMenuService;
import com.happy.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/03/20:27
 * @Description:
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有的角色")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role){
        // 判断我们的角色是否以 ROLE_ 开头，我们项目使用 Security，Security 用的角色都必须以 ROLE_ 开头
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }

        if(roleService.save(role)){
            return RespBean.success("角色添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean delRoleById(@PathVariable Integer rid){
        if(roleService.removeById(rid)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "查询所有的菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色Id查询菜单Id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>
                ().eq("rid",rid)).stream()
                .map(MenuRole::getMid).collect(Collectors.toList());
    }
}
