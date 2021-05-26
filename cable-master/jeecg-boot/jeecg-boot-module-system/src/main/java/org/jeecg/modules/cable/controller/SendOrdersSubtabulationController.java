package org.jeecg.modules.cable.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.cable.entity.SendOrdersSubtabulation;
import org.jeecg.modules.cable.service.ISendOrdersSubtabulationService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * 派单-车辆-员工关系表
 */
@Api(tags = "派单-车辆-员工关系表")
@RestController
@RequestMapping("/cable/sendOrdersSubtabulation")
public class SendOrdersSubtabulationController extends JeecgController<SendOrdersSubtabulation, ISendOrdersSubtabulationService> {
    @Autowired
    private ISendOrdersSubtabulationService sendOrdersSubtabulationService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "派单-车辆-员工关系表-分页列表查询")
    @ApiOperation(value = "派单-车辆-员工关系表-分页列表查询", notes = "派单-车辆-员工关系表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SendOrdersSubtabulation sendOrdersSubtabulation,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SendOrdersSubtabulation> queryWrapper = QueryGenerator.initQueryWrapper(sendOrdersSubtabulation, req.getParameterMap());
        Page<SendOrdersSubtabulation> page = new Page<SendOrdersSubtabulation>(pageNo, pageSize);
        IPage<SendOrdersSubtabulation> pageList = sendOrdersSubtabulationService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "派单-车辆-员工关系表-添加")
    @ApiOperation(value = "派单-车辆-员工关系表-添加", notes = "派单-车辆-员工关系表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SendOrdersSubtabulation sendOrdersSubtabulation) {
        sendOrdersSubtabulationService.save(sendOrdersSubtabulation);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "派单-车辆-员工关系表-编辑")
    @ApiOperation(value = "派单-车辆-员工关系表-编辑", notes = "派单-车辆-员工关系表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SendOrdersSubtabulation sendOrdersSubtabulation) {
        sendOrdersSubtabulationService.updateById(sendOrdersSubtabulation);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "派单-车辆-员工关系表-通过id删除")
    @ApiOperation(value = "派单-车辆-员工关系表-通过id删除", notes = "派单-车辆-员工关系表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        sendOrdersSubtabulationService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "派单-车辆-员工关系表-批量删除")
    @ApiOperation(value = "派单-车辆-员工关系表-批量删除", notes = "派单-车辆-员工关系表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.sendOrdersSubtabulationService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "派单-车辆-员工关系表-通过id查询")
    @ApiOperation(value = "派单-车辆-员工关系表-通过id查询", notes = "派单-车辆-员工关系表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        SendOrdersSubtabulation sendOrdersSubtabulation = sendOrdersSubtabulationService.getById(id);
        if (sendOrdersSubtabulation == null) return Result.error("未找到对应数据");
        return Result.ok(sendOrdersSubtabulation);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SendOrdersSubtabulation sendOrdersSubtabulation) {
        return super.exportXls(request, sendOrdersSubtabulation, SendOrdersSubtabulation.class, "派单-车辆-员工关系表");
    }

    /**
     * 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SendOrdersSubtabulation.class);
    }

}
