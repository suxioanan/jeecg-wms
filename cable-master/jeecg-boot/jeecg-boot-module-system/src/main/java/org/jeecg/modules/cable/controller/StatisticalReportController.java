package org.jeecg.modules.cable.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.entity.SendOrdersSubtabulation;
import org.jeecg.modules.cable.service.IStatisticalReportService;
import org.jeecg.modules.cable.vo.DepartureStatisticsDetailsVo;
import org.jeecg.modules.cable.vo.DepartureStatisticsVo;
import org.jeecg.modules.cable.vo.PersonnelTaskStatisticsVo;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计报表模块
 */
@Api(tags = "派单-车辆-员工关系表")
@RestController
@RequestMapping("/cable/statisticalReport")
public class StatisticalReportController extends JeecgController<SendOrdersSubtabulation, IStatisticalReportService> {
    @Autowired
    private IStatisticalReportService statisticalReportService;

    /**
     * 人员任务统计
     */
    @AutoLog(value = "派单-车辆-员工关系表-分页查询人员任务统计")
    @ApiOperation(value = "派单-车辆-员工关系表-分页查询人员任务统计", notes = "派单-车辆-员工关系表-分页查询人员任务统计")
    @GetMapping(value = "/list")
    public Result<?> getPersonnelTaskStatistics(@RequestParam(value = "taskTime", required = false) String taskTime,
                                                @RequestParam(value = "realName", required = false) String realName,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<PersonnelTaskStatisticsVo> page = new Page<>(pageNo, pageSize);
        IPage<PersonnelTaskStatisticsVo> pageList = statisticalReportService.getPersonnelTaskStatistics(taskTime, realName, page);
        return Result.ok(pageList);
    }

    /**
     * 出车统计
     */
    @AutoLog(value = "派单-车辆-员工关系表-分页查询出车统计")
    @ApiOperation(value = "派单-车辆-员工关系表-分页查询出车统计", notes = "派单-车辆-员工关系表-分页查询出车统计")
    @GetMapping(value = "/getDepartureStatisticsList")
    public Result<?> getDepartureStatistics(@RequestParam(value = "taskTime", required = false) String taskTime,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<DepartureStatisticsVo> page = new Page<>(pageNo, pageSize);
        IPage<DepartureStatisticsVo> pageList = statisticalReportService.getDepartureStatistics(taskTime, page);
        return Result.ok(pageList);
    }

    /**
     * 出车统计详情信息
     */
    @AutoLog(value = "派单-车辆-员工关系表-分页查询出车统计详情信息")
    @ApiOperation(value = "派单-车辆-员工关系表-分页查询出车统计详情信息", notes = "派单-车辆-员工关系表-分页查询出车统计详情信息")
    @GetMapping(value = "/getDepartureStatisticsDetailsList")
    public Result<?> getDepartureStatisticsDetails(@RequestParam(value = "taskTime", required = false) String taskTime,
                                                   @RequestParam(value = "license", required = false) String license,
                                                   @RequestParam(value = "month", required = false) String month,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<DepartureStatisticsDetailsVo> page = new Page<>(pageNo, pageSize);
        IPage<DepartureStatisticsDetailsVo> pageList = statisticalReportService.getDepartureStatisticsDetails(taskTime, license, month, page);
        return Result.ok(pageList);
    }

    @GetMapping(value = "/getUsers")
    public Result<?> getUsers() {
        List<SysUser> list = statisticalReportService.getUsers();
        return Result.ok(list);
    }

}
