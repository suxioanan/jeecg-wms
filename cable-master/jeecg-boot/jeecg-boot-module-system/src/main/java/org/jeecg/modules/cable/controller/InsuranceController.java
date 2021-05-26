package org.jeecg.modules.cable.controller;

import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.modules.cable.entity.Insurance;
import org.jeecg.modules.cable.entity.Vehicle;
import org.jeecg.modules.cable.service.IInsuranceService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.service.IVehicleService;
import org.jeecg.modules.cable.vo.InsuranceListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * 车保险表
 */
@Api(tags = "车保险表")
@RestController
@RequestMapping("/cable/insurance")
public class InsuranceController extends JeecgController<Insurance, IInsuranceService> {
    @Autowired
    private IInsuranceService insuranceService;
    @Autowired
    private IVehicleService vehicleService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "车保险表-分页列表查询")
    @ApiOperation(value = "车保险表-分页列表查询", notes = "车保险表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(InsuranceListVo insurance,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<InsuranceListVo> page = new Page<InsuranceListVo>(pageNo, pageSize);
        IPage<InsuranceListVo> pageList = insuranceService.getInsurancePage(insurance, page);
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "车保险表-添加")
    @ApiOperation(value = "车保险表-添加", notes = "车保险表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Insurance insurance) {
        insurance.setUpdateTime(new Date());
        insurance.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        Vehicle vehicle = vehicleService.getById(insurance.getLicense());
        insurance.setLicense(vehicle.getLicense());
        insuranceService.save(insurance);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "车保险表-编辑")
    @ApiOperation(value = "车保险表-编辑", notes = "车保险表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Insurance insurance) {
        Vehicle vehicle = vehicleService.getById(insurance.getLicense());
        insurance.setLicense(vehicle.getLicense());
        insuranceService.updateById(insurance);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "车保险表-通过id删除")
    @ApiOperation(value = "车保险表-通过id删除", notes = "车保险表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        insuranceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "车保险表-批量删除")
    @ApiOperation(value = "车保险表-批量删除", notes = "车保险表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        insuranceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "车保险表-通过id查询")
    @ApiOperation(value = "车保险表-通过id查询", notes = "车保险表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        Insurance insurance = insuranceService.getById(id);
        if (insurance == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(insurance);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Insurance insurance) {
        return super.exportXls(request, insurance, Insurance.class, "车保险表");
    }

    /**
     * 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Insurance.class);
    }

}
