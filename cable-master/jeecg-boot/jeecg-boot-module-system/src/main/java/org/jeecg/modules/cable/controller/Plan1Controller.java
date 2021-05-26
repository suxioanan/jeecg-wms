package org.jeecg.modules.cable.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.dto.Plan1Im;
import org.jeecg.modules.cable.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.vo.*;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * 计划表1
 */
@Api(tags = "计划表1")
@RestController
@RequestMapping("/cable/plan1")
public class Plan1Controller extends JeecgController<Plan1, IPlan1Service> {
    @Autowired
    private IPlan1Service plan1Service;
    @Autowired
    private IPlan2Service plan2Service;
    @Autowired
    private IPlan3Service plan3Service;
    @Autowired
    private IPlan4Service plan4Service;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * 计划1合并完单
     */
    @PostMapping(value = "/consolidationCompleted")
    public Result<?> consolidationCompleted(@RequestBody Plan1Vo plan1Vo) {
        Result<?> result = plan1Service.consolidationCompleted(Arrays.asList(plan1Vo.getPlan1Ids().split(",")), plan1Vo.getOperatorSchema(), plan1Vo.getReceiptNo(), plan1Vo.getReceiptPhotos(), plan1Vo.getTaskTime(), plan1Vo.getCompleteOrderList());
        if (result.getCode() == 200) return Result.ok(result.getMessage());
        else return Result.error(result.getMessage());
    }

    /**
     * 查询计划1批量出库完单的数据
     */
    @GetMapping(value = "/getPlan1ReceivingStorageList")
    public Result<?> getPlan1ReceivingStorageList(@RequestParam(name = "ids") String ids) {
        List<Plan1Vo> list = plan1Service.getPlan1ReceivingStorageList(Arrays.asList(ids.split(",")));
        for (Plan1Vo item : list) {
            if (!list.get(0).getProjectNo().equals(item.getProjectNo())) return Result.error("工程账号必须一致");
        }
        return Result.ok(list);
    }

    /**
     * 查询计划1批量入库完单的数据
     */
    @GetMapping(value = "/getPlan1DeliverStorage")
    public Result<?> getPlan1DeliverStorage(@RequestParam(name = "ids") String ids) {
        List<Plan1> list = plan1Service.getPlan1DeliverStorage(Arrays.asList(ids.split(",")));
        for (Plan1 item : list) {
            if (!list.get(0).getProjectNo().equals(item.getProjectNo())) return Result.error("工程账号必须一致");
        }
        return Result.ok(list);
    }

    /**
     * 分页列表查询
     */
    @AutoLog(value = "计划表1-分页列表查询")
    @ApiOperation(value = "计划表1-分页列表查询", notes = "计划表1-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Plan1Vo plan1,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<Plan1> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(plan1.getPlanType())) wrapper.like("plan_type", plan1.getPlanType());
        if (StrUtil.isNotBlank(plan1.getItemSlip())) wrapper.like("item_slip", plan1.getItemSlip());
        if (StrUtil.isNotBlank(plan1.getAssetNo())) wrapper.like("asset_no", plan1.getAssetNo());
        if (StrUtil.isNotBlank(plan1.getProjectNo())) wrapper.like("project_no", plan1.getProjectNo());
        if (StrUtil.isNotBlank(plan1.getProjectName())) wrapper.like("project_name", plan1.getProjectName());
        if (null != plan1.getCompleteState()) wrapper.eq("complete_state", plan1.getCompleteState());
        Page<Plan1> page = new Page<>(pageNo, pageSize);
        IPage<Plan1> pageList = plan1Service.page(page, wrapper);
        return Result.ok(pageList);
    }

    /**
     * 实现分页列表查询
     */
    @AutoLog(value = "计划表1-分页列表查询")
    @ApiOperation(value = "计划表1-分页列表查询", notes = "计划表1-分页列表查询")
    @GetMapping(value = "/idslistRu")
    public Result<?> idsqueryRuList(@RequestParam(name = "ids") String ids) {
        List<Plan1> pageList = plan1Service.idsqueryRuList(Arrays.asList(ids.split(",")));
        for (Plan1 plan1 : pageList) {
            if (!pageList.get(0).getProjectNo().equals(plan1.getProjectNo())) return Result.error("工程账号必须一致");
        }
        return Result.ok(pageList.stream().peek(plan1 -> plan1.setBackup1(null)).collect(Collectors.toList()));
    }

    /**
     * 派单出库列表查询
     */
    @AutoLog(value = "计划表1-分页列表查询")
    @ApiOperation(value = "计划表1-分页列表查询", notes = "计划表1-分页列表查询")
    @GetMapping(value = "/idslistChu")
    public Result<?> idsqueryChuList(@RequestParam(name = "ids") String ids) {
        List<SendOrdersVo> pageList = plan1Service.idsqueryChuList(Arrays.asList(ids.split(",")));
        for (SendOrdersVo sendOrdersVo : pageList) {
            if (!pageList.get(0).getProjectNo().equals(sendOrdersVo.getProjectNo())) return Result.error("工程账号必须一致");
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "计划表1-添加")
    @ApiOperation(value = "计划表1-添加", notes = "计划表1-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Plan1 plan1) {
        plan1.setUpdateTime(new Date());
        plan1.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan1Service.save(plan1);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "计划表1-编辑")
    @ApiOperation(value = "计划表1-编辑", notes = "计划表1-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Plan1 plan1) {
        plan1.setUpdateTime(new Date());
        plan1.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan1Service.updateById(plan1);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "计划表1-通过id删除")
    @ApiOperation(value = "计划表1-通过id删除", notes = "计划表1-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        if (plan1Service.getById(id).getSendOrdersState() == 0) {
            plan1Service.removeById(id);
            return Result.ok("删除成功!");
        }
        return Result.error("该计划已派过单，暂时不能删除");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "计划表1-通过id查询")
    @ApiOperation(value = "计划表1-通过id查询", notes = "计划表1-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        Plan1 plan1 = plan1Service.getById(id);
        if (plan1 == null) return Result.error("未找到对应数据");
        return Result.ok(plan1);
    }

    /**
     * 导出 plan1
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(Plan1Im plan1Im, @RequestParam(name = "explain", required = false) String explain) {
        // 获取导出数据集
        List<Plan1Im> list = plan1Service.exportPlan1(plan1Im, explain);
        // AutoPoi 导出 excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.CLASS, Plan1Im.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams());
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

    /**
     * 通过excel导入数据
     */
    @Transactional
    @RequestMapping(value = "/importExcel/{planType}", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, @PathVariable String planType) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Material material = new Material();
        if (null != SysUserConstant.SYS_USER) {
            material.setCreateBy(SysUserConstant.SYS_USER.getUsername());
            material.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        }
        material.setCreateTime(new Date());
        material.setUpdateTime(new Date());
        int in = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Plan1Im> list = ExcelImportUtil.importExcel(file.getInputStream(), Plan1Im.class, params);
                Plan1 plan1 = new Plan1();
                for (Plan1Im plan1Im : list) {

                    //判断对象中属性值是否为空
                    boolean flag = (StrUtil.isNotBlank(plan1Im.getProjectName()) ||
                            StrUtil.isNotBlank(plan1Im.getProjectNo()) || StrUtil.isNotBlank(plan1Im.getWasteMaterialCode())
                            || StrUtil.isNotBlank(plan1Im.getWasteMaterialText()) || StrUtil.isNotBlank(plan1Im.getAssetNo()));
                    if (!flag) return Result.error("请补全物料信息！");

                    // 单位之间的转换
                    List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
                    for (SysDictItem dictItem : dictItems) {
                        // 导入时判断单位是否存在，存在则进行单位转换
                        if (StrUtil.isNotBlank(plan1Im.getRawMaterialUnit())) {
                            if (plan1Im.getRawMaterialUnit().trim().equals(dictItem.getItemText())) {
                                plan1.setRawMaterialUnit(Integer.parseInt(dictItem.getItemValue()));
                            }
                        }
                        if (StrUtil.isNotBlank(plan1Im.getWasteMaterialUnit())) {
                            if (plan1Im.getWasteMaterialUnit().trim().equals(dictItem.getItemText())) {
                                plan1.setWasteMaterialUnit(Integer.parseInt(dictItem.getItemValue()));
                            }
                        }
                    }
                    // 属性拷贝 将 plan1Im 接收的excel数据转换为 plan1 实体类数据
                    BeanUtils.copyProperties(plan1Im, plan1);
                    plan1.setPlanType(planType);
                    plan1.setSendOrdersState(0);
                    plan1.setCompleteState(0);
                    plan1.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
                    plan1.setCreateBy(SysUserConstant.SYS_USER.getUsername());
                    plan1.setUpdateTime(new Date());
                    plan1.setCreateTime(new Date());
                    plan1Service.save(plan1);
                    in++;
                    QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("serial", plan1.getWasteMaterialCode());
                    queryWrapper.eq("name", plan1.getWasteMaterialText());
                    List<Material> list1 = materialService.list(queryWrapper);
                    if (list1.size() == 0) {
                        if (StrUtil.isNotBlank(plan1.getWasteMaterialCode()) && StrUtil.isNotBlank(plan1.getWasteMaterialText())) {
                            // 废旧物料编码作为物料编码
                            material.setSerial(plan1.getWasteMaterialCode());
                            // 废旧物料描述作为物料名称
                            material.setName(plan1.getWasteMaterialText());
                            // 废旧物料单位作为物料单位
                            material.setUnit(plan1.getWasteMaterialUnit());
                            materialService.save(material);
                        }
                    }
                }
                if (in != 0) {
                    return Result.ok("文件导入成功！数据行数：" + in);
                }
                return Result.ok("文件导入成功！数据行数：" + list.size());
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

    @PutMapping(value = "/planTheSameDayEdit")
    public Result<?> planTheSameDayEdit(@RequestBody SendOrdersVo sendOrdersVo) {
        switch (sendOrdersVo.getPlanType()) {
            case "1":
                Plan1 plan1 = plan1Service.getById(sendOrdersVo.getPlanId());
                if (null != SysUserConstant.SYS_USER) plan1.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
                plan1.setUpdateTime(new Date());
                plan1.setTheContact(sendOrdersVo.getLinkman());
                plan1.setThePhone(sendOrdersVo.getPhone());
                plan1.setEngineeringAddress(sendOrdersVo.getAddress());
                plan1Service.updateById(plan1);
                break;
            case "2":
                Plan2 plan2 = plan2Service.getById(sendOrdersVo.getPlanId());
                if (null != SysUserConstant.SYS_USER) plan2.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
                plan2.setUpdateTime(new Date());
                plan2.setEquipmentOwners(sendOrdersVo.getLinkman());
                plan2.setAddress(sendOrdersVo.getAddress());
                plan2Service.updateById(plan2);
                break;
            case "3":
                Plan3 plan3 = plan3Service.getById(sendOrdersVo.getPlanId());
                if (null != SysUserConstant.SYS_USER) plan3.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
                plan3.setUpdateTime(new Date());
                plan3.setFieldConsignee(sendOrdersVo.getLinkman());
                plan3.setCPhone(sendOrdersVo.getLinkman());
                plan3.setAddress(sendOrdersVo.getAddress());
                plan3Service.updateById(plan3);
                break;
            case "4":
                Plan4 plan4 = plan4Service.getById(sendOrdersVo.getPlanId());
                if (null != SysUserConstant.SYS_USER) plan4.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
                plan4.setUpdateTime(new Date());
                plan4.setTeamContact(sendOrdersVo.getLinkman());
                plan4.setSamplingAddres(sendOrdersVo.getAddress());
                plan4Service.updateById(plan4);
                break;
            default:
                throw new JeecgBootException(this.getClass().getName() + "没有对应处理项~");
        }
        return Result.ok("修改成功!");
    }

    /**
     * 查询已完成的计划（结算）
     */
    @GetMapping(value = "/selectSettleAccounts")
    public Result<?> selectSettleAccounts(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "backup1", required = false) String backup1,
                                          @RequestParam(name = "planType", required = false) String planType,
                                          @RequestParam(name = "projectNo", required = false) String projectNo) {
        Page<SettleAccountsVo> page = new Page<>(pageNo, pageSize);
        IPage<SettleAccountsVo> pageList = plan1Service.selectSettleAccounts(backup1, planType, projectNo, page);
        return Result.ok(pageList);
    }

    /**
     * 查询已完成的计划(结算)详情信息
     */
    @GetMapping(value = "/selectSettleAccountsDetails")
    public Result<?> selectSettleAccountsDetails(@RequestParam(name = "projectNo", required = false) String projectNo,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SettleAccountsDetailsVo> page = new Page<>(pageNo, pageSize);
        IPage<SettleAccountsDetailsVo> pageList = plan1Service.selectSettleAccountsDetails(projectNo, page);
        return Result.ok(pageList);
    }

    /**
     * 项目结算，计划结算功能
     */
    @GetMapping(value = "/updateSettleAccounts")
    public Result<?> updateSettleAccounts(@RequestParam(name = "projectNo", required = false) String projectNo) {
        if (null == SysUserConstant.SYS_USER) return Result.error("用户登录超时，请重新登录!");
        Plan1 plan1 = new Plan1();
        plan1.setBackup1("1");
        plan1.setUpdateTime(new Date());
        plan1.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan1Service.update(plan1, new QueryWrapper<Plan1>().eq("project_no", projectNo));
        Plan2 plan2 = new Plan2();
        plan2.setBackup1("1");
        plan2.setUpdateTime(new Date());
        plan2.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan2Service.update(plan2, new QueryWrapper<Plan2>().eq("project_no", projectNo));
        Plan3 plan3 = new Plan3();
        plan3.setBackup1("1");
        plan3.setUpdateTime(new Date());
        plan3.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan3Service.update(plan3, new QueryWrapper<Plan3>().eq("project_no", projectNo));
        Plan4 plan4 = new Plan4();
        plan4.setBackup1("1");
        plan4.setUpdateTime(new Date());
        plan4.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan4Service.update(plan4, new QueryWrapper<Plan4>().eq("project_no", projectNo));
        return Result.ok("结算成功!");
    }

    /**
     * 计划表1配变电统计
     */
    @GetMapping(value = "/selectSubstation")
    public Result<?> selectSubstation(@RequestParam(name = "wasteMaterialText", required = false) String wasteMaterialText,
                                      @RequestParam(name = "beginTime", required = false) String beginTime,
                                      @RequestParam(name = "endTime", required = false) String endTime,
                                      @RequestParam(name = "planType", required = false) String planType,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Plan1Vo> page = new Page<>(pageNo, pageSize);
        IPage<Plan1Vo> pageList = plan1Service.selectSubstation(wasteMaterialText, beginTime, endTime, planType, page);
        return Result.ok(pageList);
    }

    /**
     * 导出变电/导线统计数据 excel
     */
    @RequestMapping(value = "/exportXls2")
    public ModelAndView exportXls2(Plan1ExcelVo plan1ExcelVo) {
        List<Plan1ExcelVo> list = plan1Service.exportPlan2(plan1ExcelVo);
        Plan1ExcelVo plan1ExcelVo1 = new Plan1ExcelVo();
        BigDecimal stockNum = BigDecimal.valueOf(0);
        BigDecimal receivingNum = BigDecimal.valueOf(0);
        StringBuilder proName = new StringBuilder();
        for (Plan1ExcelVo excelVo : list) {
            proName.append(excelVo.getProjectName()).append(",");
            stockNum = stockNum.add(BigDecimal.valueOf(Double.parseDouble(excelVo.getStockNum())));
            receivingNum = receivingNum.add(BigDecimal.valueOf(Double.parseDouble(excelVo.getReceivingNum())));
            excelVo.setProjectName(null);
        }
        plan1ExcelVo1.setWasteMaterialText("合计:");
        plan1ExcelVo1.setStockNum(stockNum.toString());
        plan1ExcelVo1.setReceivingNum(receivingNum.toString());
        list.add(plan1ExcelVo1);
        list.get(0).setDecommissioningDate(plan1ExcelVo.getDecommissioningDate());
        list.get(0).setRemark(plan1ExcelVo.getRemark());
        list.get(0).setProjectName(proName.toString());
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.CLASS, Plan1ExcelVo.class);
        if (plan1ExcelVo.getPlanType().equals("线路")) {
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(plan1ExcelVo.getBeginTime() + "--" + plan1ExcelVo.getEndTime() + "/线路统计", ""));
        } else {
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(plan1ExcelVo.getBeginTime() + "--" + plan1ExcelVo.getEndTime() + "/变电统计", ""));
        }
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

}
