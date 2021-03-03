package com.happy.server.controller;


import com.happy.server.common.RespBean;
import com.happy.server.pojo.Joblevel;
import com.happy.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "获取所有的职称")
    @GetMapping("/")
    public List<Joblevel> getAllJobLevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if(joblevelService.save(joblevel)){
            return RespBean.success("职称添加成功");
        }
        return RespBean.error("职称添加失败");
    }

    @ApiOperation(value = "职称修改")
    @PutMapping("/")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel){
        if(joblevelService.updateById(joblevel)){
            return RespBean.success("职称修改成功");
        }
        return RespBean.error("职称修改失败");
    }

    @ApiOperation(value = "删除职称")
    @DeleteMapping("/{id}")
    public RespBean delJobLevelById(@PathVariable Integer id){
        if(joblevelService.removeById(id)){
            return RespBean.success("职称删除成功");
        }
        return RespBean.error("职称删除失败");
    }

    @ApiOperation(value = "批量删除职称")
    @DeleteMapping("/")
    public RespBean delJobLevelByIds(Integer [] ids){
        if(joblevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("职称批量删除成功");
        }
        return RespBean.error("职称批量删除失败");
    }


}
