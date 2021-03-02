package com.happy.server.config.security;

import com.happy.server.pojo.Menu;
import com.happy.server.pojo.Role;
import com.happy.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/02/15:35
 * @Description: 权限控制，根据请求 url 分析请求所需要的角色
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 获取请求 url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        // 获取菜单需要的角色
        List<Menu> menusWithRole = menuService.getMenusWithRole();
        for (Menu menu : menusWithRole) {
            // 判断请求 url 与菜单角色是否匹配
            if(antPathMatcher.match(menu.getUrl(),requestUrl)){
                // 从 menu 中获取角色，然后转换成 String 数组
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        // 没匹配的 url 默认登录即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
