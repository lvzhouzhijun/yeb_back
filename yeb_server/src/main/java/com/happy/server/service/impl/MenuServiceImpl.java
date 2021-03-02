package com.happy.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.server.mapper.MenuMapper;
import com.happy.server.pojo.Admin;
import com.happy.server.pojo.Menu;
import com.happy.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getMenusByAdminId() {
        // 获取用户Id
        Integer id = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return menuMapper.getMenusByAdminId(id);
    }
}
