package com.happy.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/02/28/21:32
 * @Description: JWT 登录授权过滤器，每一次请求都会进行过滤
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 token
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        // 判断 token 不等于 null，并且 开头必须是，我们配置文件配置的 Bearer
        if(null != authHeader && authHeader.startsWith(tokenHead)){
            // 截取 token
            String authToken = authHeader.substring(tokenHead.length());
            // 根据 token 获取用户名
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            // token 存在用户名但未登录
            if(null != username && null == SecurityContextHolder.getContext().getAuthentication()){
                // 登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 验证 token 是否有效，重新设置用户对象
                if(jwtTokenUtil.validateToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        // 放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
