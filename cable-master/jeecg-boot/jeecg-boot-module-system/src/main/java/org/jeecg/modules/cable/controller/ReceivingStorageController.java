package org.jeecg.modules.cable.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.cable.entity.ReceivingStorage;
import org.jeecg.modules.cable.service.IReceivingStorageService;

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
 * 出库/完单表
 */
@Api(tags = "出库/完单表")
@RestController
@RequestMapping("/cable/receivingStorage")
public class ReceivingStorageController extends JeecgController<ReceivingStorage, IReceivingStorageService> {
    @Autowired
    private IReceivingStorageService receivingStorageService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "出库/完单表-分页列表查询")
    @ApiOperation(value = "出库/完单表-分页列表查询", notes = "出库/完单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ReceivingStorage receivingStorage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ReceivingStorage> queryWrapper = QueryGenerator.initQueryWrapper(receivingStorage, req.getParameterMap());
        Page<ReceivingStorage> page = new Page<ReceivingStorage>(pageNo, pageSize);
        IPage<ReceivingStorage> pageList = receivingStorageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "出库/完单表-添加")
    @ApiOperation(value = "出库/完单表-添加", notes = "出库/完单表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ReceivingStorage receivingStorage) {
        receivingStorageService.save(receivingStorage);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "出库/完单表-编辑")
    @ApiOperation(value = "出库/完单表-编辑", notes = "出库/完单表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ReceivingStorage receivingStorage) {
        receivingStorageService.updateById(receivingStorage);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "出库/完单表-通过id删除")
    @ApiOperation(value = "出库/完单表-通过id删除", notes = "出库/完单表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        receivingStorageService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "出库/完单表-批量删除")
    @ApiOperation(value = "出库/完单表-批量删除", notes = "出库/完单表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.receivingStorageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "出库/完单表-通过id查询")
    @ApiOperation(value = "出库/完单表-通过id查询", notes = "出库/完单表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        ReceivingStorage receivingStorage = receivingStorageService.getById(id);
        if (receivingStorage == null) return Result.error("未找到对应数据");
        return Result.ok(receivingStorage);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReceivingStorage receivingStorage) {
        return super.exportXls(request, receivingStorage, ReceivingStorage.class, "出库/完单表");
    }

    /**
     * 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ReceivingStorage.class);
    }

}
