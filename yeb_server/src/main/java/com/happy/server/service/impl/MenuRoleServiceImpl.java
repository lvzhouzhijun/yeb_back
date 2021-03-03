package com.happy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.server.common.RespBean;
import com.happy.server.mapper.MenuRoleMapper;
import com.happy.server.pojo.MenuRole;
import com.happy.server.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2021-02-25
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {


    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    @Transactional
    @Override
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        // 删除该角色的所有Id
        menuRoleMapper.delete(new QueryWrapper<MenuRole>()
            .eq("rid",rid));
        if(null == mids || 0 == mids.length){
            return RespBean.success("更新成功");
        }
        // 批量更新
        menuRoleMapper.insertRecord(rid,mids);
        return null;
    }
}
