package com.happy.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.server.mapper.MenuMapper;
import com.happy.server.pojo.Admin;
import com.happy.server.pojo.Menu;
import com.happy.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public List<Menu> getMenusByAdminId() {
        // 获取用户Id
        Integer id = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 从Redis中获取菜单数据
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + id);
        // 如果为空，从数据库获取
        if(CollectionUtils.isEmpty(menus)){
            menus = menuMapper.getMenusByAdminId(id);
            // 将数据设置到Redis中
            valueOperations.set("menu_"+id,menus);
        }
        return menus;
    }
}
