package org.jeecg.modules.cable.controller;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.modules.cable.entity.Material;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan2;
import org.jeecg.modules.cable.entity.Plan3;
import org.jeecg.modules.cable.service.IMaterialService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.service.IPlan1Service;
import org.jeecg.modules.cable.service.IPlan2Service;
import org.jeecg.modules.cable.service.IPlan3Service;
import org.jeecg.modules.cable.vo.AnnualReportVo;
import org.jeecg.modules.cable.vo.MaterialOutPutAccountVo;
import org.jeecg.modules.cable.vo.MaterialRemainingAccountVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * 物料表
 */
@Api(tags = "物料表")
@RestController
@RequestMapping("/cable/material")
public class MaterialController extends JeecgController<Material, IMaterialService> {
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IPlan1Service plan1Service;
    @Autowired
    private IPlan2Service plan2Service;
    @Autowired
    private IPlan3Service plan3Service;

    /**
     * 物料数据重复校验方法
     */
    private boolean dataCheck(Object object) {
        Material material = (Material) object;
        int count = materialService.count(new QueryWrapper<Material>().eq("serial", material.getSerial()).ne("id", material.getId()));
        return count > 0;
    }

    /**
     * 物料出入库台账
     */
    @AutoLog(value = "物料出入库台账")
    @ApiOperation(value = "物料出入库台账", notes = "物料出入库台账")
    @GetMapping(value = "/getMaterialOutPutAccountList")
    public Result<?> getMaterialOutPutAccountList(MaterialOutPutAccountVo materialOutPutAccountVo,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<MaterialOutPutAccountVo> page = new Page<>(pageNo, pageSize);
        IPage<MaterialOutPutAccountVo> pageList = materialService.getMaterialOutPutAccountList(materialOutPutAccountVo, page);
        return Result.ok(pageList);
    }

    /**
     * 物料余留台账统计
     */
    @AutoLog(value = "物料表-分页查询物料余留台账统计")
    @ApiOperation(value = "物料表-分页查询物料余留台账统计", notes = "物料表-分页查询物料余留台账统计")
    @GetMapping(value = "/getMaterialRemainingAccountList")
    public Result<?> getMaterialRemainingAccountList(@RequestParam(name = "serial", required = false) String serial,
                                                     @RequestParam(name = "name", required = false) String name,
                                                     @RequestParam(name = "projectNo", required = false) String projectNo,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<MaterialRemainingAccountVo> page = new Page<>(pageNo, pageSize);
        IPage<MaterialRemainingAccountVo> pageList = materialService.getMaterialRemainingAccountList(serial, name, projectNo, page);
        return Result.ok(pageList);
    }

    /**
     * 年度出入台账分页信息
     */
    @AutoLog(value = "物料表-分页查询物料年度出入台账统计")
    @ApiOperation(value = "物料表-分页查询物料年度出入台账统计", notes = "物料表-分页查询物料年度出入台账统计")
    @GetMapping(value = "/getAnnualAccountList")
    public Result<?> getAnnualAccountList(@RequestParam(name = "planType", required = false) String planType,
                                          @RequestParam(name = "dateTime", required = false) String dateTime,
                                          @RequestParam(name = "serial", required = false) String serial,
                                          @RequestParam(name = "name", required = false) String name,
                                          @RequestParam(name = "projectNo", required = false) String projectNo,
                                          @RequestParam(name = "assetNo", required = false) String assetNo,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<AnnualReportVo> page = new Page<AnnualReportVo>(pageNo, pageSize);
        IPage<AnnualReportVo> pageList = materialService.getAnnualAccountList(planType, dateTime, serial, name, projectNo, assetNo, page);
        return Result.ok(pageList);
    }

    /**
     * 物料管理分页信息
     */
    @AutoLog(value = "物料表-分页列表查询")
    @ApiOperation(value = "物料表-分页列表查询", notes = "物料表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "serial", required = false) String serial,
                                   @RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "supplier", required = false) String supplier,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Material> page = new Page<>(pageNo, pageSize);
        IPage<Material> pageList = materialService.getMaterialPageList(serial, name, supplier, page);
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "物料表-添加")
    @ApiOperation(value = "物料表-添加", notes = "物料表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Material material) {
        if (dataCheck(material)) return Result.error("物料编号重复！");
        material.setUpdateTime(new Date());
        material.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        materialService.save(material);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "物料表-编辑")
    @ApiOperation(value = "物料表-编辑", notes = "物料表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Material material) {
        if (dataCheck(material)) return Result.error("物料编号重复！");
        if (null == SysUserConstant.SYS_USER) return Result.error("请先登录！");
        Material one = materialService.getById(material.getId());
        material.setUpdateTime(new Date());
        material.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        if (one.getSerial() != null && one.getSerial().equals("cable2") && one.getName().equals("电缆2")) {
            // 对于电缆2的修改操作做单独处理
            // 不能修改电缆2的物料编码以及物料名称等信息
            materialService.editMaterial(material);
            return Result.ok("电缆2默认不能修改物料编码以及物料名称！");
        }
        materialService.updateById(material);
        return Result.ok("修改成功！");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "物料表-通过id删除")
    @ApiOperation(value = "物料表-通过id删除", notes = "物料表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        // 获取到物料对象
        Material material = materialService.getById(id);

        // 若四个计划中存在物料对象信息则不能删除
        QueryWrapper<Plan1> plan1QueryWrapper = new QueryWrapper<>();
        QueryWrapper<Plan2> plan2QueryWrapper = new QueryWrapper<>();
        QueryWrapper<Plan3> plan3QueryWrapper = new QueryWrapper<>();
        // 根据物料编码查询是否存在于计划表1中
        plan1QueryWrapper.eq("raw_material_code", material.getSerial());
        // 根据物料编码查询是否存在于计划表2中
        plan2QueryWrapper.eq("asset_no", material.getSerial());
        // 根据物料编码查询是否存在于计划表3中
        plan3QueryWrapper.eq("material_code", material.getSerial());

        List<Plan1> plan1 = plan1Service.list(plan1QueryWrapper);
        List<Plan2> plan2 = plan2Service.list(plan2QueryWrapper);
        List<Plan3> plan3 = plan3Service.list(plan3QueryWrapper);

        if (plan1.size() > 0 || plan2.size() > 0 || plan3.size() > 0)
            return Result.error("物料暂被派单使用,暂不能删除!");

        // 电缆2的物料信息不可以被删除
        if (StrUtil.isNotBlank(material.getSerial()) && material.getSerial().equals("cable2"))
            return Result.error("电缆2物料暂不能删除!");

        // 否则可以进行删除
        materialService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "物料表-批量删除")
    @ApiOperation(value = "物料表-批量删除", notes = "物料表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.materialService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "物料表-通过id查询")
    @ApiOperation(value = "物料表-通过id查询", notes = "物料表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        Material material = materialService.getById(id);
        if (material == null) return Result.error("未找到对应数据");
        return Result.ok(material);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Material material) {
        return super.exportXls(request, material, Material.class, "物料表");
    }

    /**
     * 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Material> list = ExcelImportUtil.importExcel(file.getInputStream(), Material.class, params);
                long start = System.currentTimeMillis();
                materialService.saveBatch(list);
                return Result.ok("文件导入成功！数据行数：" + list.size() + ",消耗时间 :" + (System.currentTimeMillis() - start) + "毫秒");
            } catch (Exception e) {
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 物料出入统计柱形图
     */
    @GetMapping(value = "/materialOutPutList")
    public Result<?> materialOutPutList(MaterialOutPutAccountVo materialOutPutAccountVo) {
        List<Map<String, String>> list = materialService.materialOutPutList(materialOutPutAccountVo);
        return Result.ok(list);
    }
}
