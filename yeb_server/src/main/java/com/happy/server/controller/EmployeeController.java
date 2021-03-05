package com.happy.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.happy.server.common.RespBean;
import com.happy.server.common.RespPageBean;
import com.happy.server.pojo.*;
import com.happy.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
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
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private INationService nationService;

    @Autowired
    private IPoliticsStatusService politicsStatusService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IJoblevelService joblevelService;

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "获取所有员工分页数据")
    @GetMapping("/")
    public RespPageBean getEmployee(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10")Integer size,
            Employee employee, LocalDate [] beginDateScope){
        return employeeService.getEmployeeByPage(currentPage,size,employee,beginDateScope);
    }

    @ApiOperation(value = "获取所有的职位")
    @GetMapping("/positions")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "获取所有的部门")
    @GetMapping("/deps")
    private List<Department> getAllDepartment(){
        return departmentService.list();
    }

    @ApiOperation(value = "查询出所有的名族")
    @GetMapping("/nations")
    public List<Nation> getAllNation(){
        return nationService.list();
    }

    @ApiOperation(value = "查询出所有的政治面貌")
    @GetMapping("/politicsstatus")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value = "查询出所有的职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJoblevel(){
        return joblevelService.list();
    }

    @GetMapping("/maxWorkID")
    @ApiOperation(value = "查询出工号")
    public RespBean maxWorkID() {
        return employeeService.maxWorkID();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee){
        return employeeService.addEmp(employee);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee){
        if(employeeService.updateById(employee)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean delEmp(@PathVariable Integer id){
        if(employeeService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void exportEmp(HttpServletResponse response){
        List<Employee> employees = employeeService.getEmployee(null);
        // 参数一：是文件名
        // 参数二：是 Excel 的 sheet 名称
        // 参数三：Excel 类型
        ExportParams exportParams = new ExportParams("员工表",
                "员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Employee.class, employees);
        ServletOutputStream outputStream = null;
        try {
            // 流形式
            response.setHeader("content-type","application/octet-stream");
            // 防止中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工表.xls","UTF-8"));
            // 获取输出流
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        // 去掉标题行
        params.setTitleRows(1);
        // 查询出所有的民族
        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(
                    file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                // new Nation(employee.getNation().getName()) 这一部分使用到有参构造
                // nationList.indexOf() 我们重写了 hashCode 和 equals 方法，使用 name 去比较了
                Integer nationId = nationList.get(
                        nationList.indexOf(
                                new Nation(employee.getNation().getName()))).getId();
                employee.setNationId(nationId);
                // 政治面貌Id
                Integer politicsStatusId = politicsStatusList.get(politicsStatusList.indexOf(
                        new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId();
                employee.setPoliticId(politicsStatusId);
                // 部门Id
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(
                        new Department(employee.getDepartment().getName())
                )).getId());
                // 职称Id
                employee.setJobLevelId(joblevelList.get(joblevelList.indexOf(
                        new Joblevel(employee.getJoblevel().getName()))).getId());
                // 职位Id
                employee.setPosId(positionList.get(positionList
                        .indexOf(new Position(employee.getPosition().getName()))).getId());
            });
            if(employeeService.saveBatch(list)){
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}
