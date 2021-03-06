package com.happy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.server.common.AdminUtils;
import com.happy.server.common.RespBean;
import com.happy.server.config.security.JwtTokenUtil;
import com.happy.server.mapper.AdminMapper;
import com.happy.server.mapper.AdminRoleMapper;
import com.happy.server.mapper.RoleMapper;
import com.happy.server.pojo.Admin;
import com.happy.server.pojo.AdminRole;
import com.happy.server.pojo.Role;
import com.happy.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        if(StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码输入错误，请重新输入");
        }
        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(null == userDetails || !passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或密码不正确");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁用，请联系管理员");
        }
        // 更新 security 登录用户对象
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // 生成 token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登录成功，获取token",tokenMap);
    }

    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(
                new QueryWrapper<Admin>().eq("username",username)
        .eq("enabled",true));
    }

    @Override
    public List<Role> getRole(Integer adminId) {

        return roleMapper.getRoles(adminId);
    }

    @Override
    public List<Admin> getAllAdmins(String keywords) {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(),keywords);
    }

    /**
     * 更新操作员角色
     * @param adminId 操作员Id
     * @param rids 角色Id
     * @return
     */
    @Transactional
    @Override
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        // 先删除操作员的所有角色
        adminRoleMapper.delete(new QueryWrapper<AdminRole>()
            .eq("adminId",adminId));
        // 在根据传进来的 角色Id，给操作员添加角色
        Integer result = adminRoleMapper.addAdminRole(adminId, rids);
        if(rids.length == result){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        if(passwordEncoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(passwordEncoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if(1 == result){
                return RespBean.success("密码更新成功");
            }
        }
        return RespBean.error("密码更新失败");
    }

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if(1 == result){
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            // 更新全局Security  对象
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities())
            );
            return RespBean.success("头像更新成功",url);
        }
        return RespBean.error("更新失败");
    }
}
