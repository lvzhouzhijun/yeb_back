package com.happy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy.server.pojo.Admin;
import com.happy.server.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 获取所有的操作员
     * @param id 不包括当前登录的操作员
     * @param keywords 根据名称获取
     * @return
     */
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);
}
