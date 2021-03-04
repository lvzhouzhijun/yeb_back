package com.happy.server.common;

import com.happy.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/04/18:12
 * @Description:
 */
public class AdminUtils {

    public static Admin getCurrentAdmin(){
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return admin;
    }

}
