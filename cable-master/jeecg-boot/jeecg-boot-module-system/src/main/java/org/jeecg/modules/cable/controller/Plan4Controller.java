package org.jeecg.modules.cable.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan4;
import org.jeecg.modules.cable.dto.Plan4Im;
import org.jeecg.modules.cable.service.IPlan4Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.vo.Plan4ExcelVo;
import org.jeecg.modules.cable.vo.Plan4Vo;
import org.jeecg.modules.cable.vo.SendOrdersVo;
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
 * 计划表4
 */
@Api(tags = "计划表4")
@RestController
@RequestMapping("/cable/plan4")
public class Plan4Controller extends JeecgController<Plan4, IPlan4Service> {
    @Autowired
    private IPlan4Service plan4Service;

    /**
     * 计划4合并完单
     */
    @PostMapping(value = "/consolidationCompleted")
    public Result<?> consolidationCompleted(@RequestBody Plan4Vo plan4Vo) {
        Result<?> result = plan4Service.consolidationCompleted(Arrays.asList(plan4Vo.getPlan4Ids().split(",")), plan4Vo.getOperatorSchema(), plan4Vo.getPlan4ReceiptNo(), plan4Vo.getReceiptPhotos(), plan4Vo.getTaskTime(), plan4Vo.getCompleteOrderList());
        if (result.getCode().equals(CommonConstant.SC_OK_200)) return Result.ok(result.getMessage());
        return Result.error(result.getMessage());
    }

    /**
     * 查询计划4批量出库完单的数据
     */
    @GetMapping(value = "/getPlan4ReceivingStorageList")
    public Result<?> getPlan4ReceivingStorageList(@RequestParam(name = "ids") String ids) {
        List<Plan4Vo> list = plan4Service.getPlan4ReceivingStorageList(Arrays.asList(ids.split(",")));
        for (Plan4Vo item : list) {
            if (!list.get(0).getProjectNo().equals(item.getProjectNo())) return Result.error("工程账号必须一致");
        }
        return Result.ok(list);
    }

    /**
     * 查询计划4批量入库完单的数据
     */
    @GetMapping(value = "/getPlan4DeliverStorage")
    public Result<?> getPlan4DeliverStorage(@RequestParam(name = "ids") String ids) {
        List<Plan4> list = plan4Service.getPlan4DeliverStorage(Arrays.asList(ids.split(",")));
        for (Plan4 item : list) {
            if (!list.get(0).getProjectNo().equals(item.getProjectNo())) return Result.error("工程账号必须一致");
        }
        return Result.ok(list);
    }

    /**
     * 分页列表查询
     */
    @AutoLog(value = "计划表4-分页列表查询")
    @ApiOperation(value = "计划表4-分页列表查询", notes = "计划表4-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Plan4 plan4,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Plan4> page = new Page<Plan4>(pageNo, pageSize);
        IPage<Plan4> pageList = plan4Service.pageList(plan4, page);
        return Result.ok(pageList);
    }

    /**
     * 实现分页列表查询
     */
    @AutoLog(value = "计划表1-分页列表查询")
    @ApiOperation(value = "计划表1-分页列表查询", notes = "计划表1-分页列表查询")
    @GetMapping(value = "/idslistRu")
    public Result<?> idsqueryRuList(@RequestParam(name = "ids") String ids) {
        List<Plan1> list = new ArrayList<>();
        Plan1 plan;
        List<Plan4> pageList = plan4Service.idsqueryRuList(Arrays.asList(ids.split(",")));
        for (Plan4 plan4 : pageList) {
            if (!pageList.get(0).getProjectNo().equals(plan4.getProjectNo())) return Result.error("工程账号必须一致");
            plan = new Plan1();
            plan.setId(plan4.getId());                                //计划id
            plan.setProjectNo(plan4.getProjectNo());                  //工程账号
            plan.setProjectName(plan4.getEngName());                  //项目名称
            //TODO 电缆的"物料描述"为 "电缆名称" + "电压等级" + "电缆截面"
            String material = plan4.getCableName() + " " + plan4.getVoltageGrade() + " " + plan4.getCableCross();
            plan.setWasteMaterialText(material);                      //物料描述
            plan.setWasteMaterialCode(material);                      //物料代码
            plan4.setBackup1(null);
            list.add(plan);
        }
        return Result.ok(list);

    }

    /**
     * 派单出库列表查询
     */
    @AutoLog(value = "计划表1-分页列表查询")
    @ApiOperation(value = "计划表1-分页列表查询", notes = "计划表1-分页列表查询")
    @GetMapping(value = "/idslistChu")
    public Result<?> idsqueryChuList(@RequestParam(name = "ids") String ids) {
        List<SendOrdersVo> pageList = plan4Service.idsqueryChuList(Arrays.asList(ids.split(",")));
        for (SendOrdersVo sendOrdersVo : pageList) {
            if (!pageList.get(0).getProjectNo().equals(sendOrdersVo.getProjectNo())) return Result.error("工程账号必须一致");
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "计划表4-添加")
    @ApiOperation(value = "计划表4-添加", notes = "计划表4-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Plan4 plan4) {
        plan4.setUpdateTime(new Date());
        plan4.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan4Service.save(plan4);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "计划表4-编辑")
    @ApiOperation(value = "计划表4-编辑", notes = "计划表4-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Plan4 plan4) {
        plan4.setUpdateTime(new Date());
        plan4.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
        plan4Service.updateById(plan4);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "计划表4-通过id删除")
    @ApiOperation(value = "计划表4-通过id删除", notes = "计划表4-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        Plan4 plan4 = plan4Service.getById(id);
        if (plan4.getSendOrdersState() == 0) {
            plan4Service.removeById(id);
            return Result.ok("删除成功!");
        }
        return Result.error("该计划已派过单，暂时不能删除");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "计划表4-通过id查询")
    @ApiOperation(value = "计划表4-通过id查询", notes = "计划表4-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        Plan4 plan4 = plan4Service.getById(id);
        if (plan4 == null) return Result.error("未找到对应数据");
        return Result.ok(plan4);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(Plan4 plan4,
                                  @RequestParam(name = "explain", required = false) String explain,
                                  @RequestParam(name = "beginTime", required = false) String beginTime,
                                  @RequestParam(name = "endTime", required = false) String endTime) {
        // 获取导出数据集
        List<Plan4Im> list = plan4Service.exportPlan4(plan4, explain, beginTime, endTime);
        // 导出 excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.CLASS, Plan4Im.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams());
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

    /**
     * 导出汇总数据 excel
     */
    @RequestMapping(value = "/exportXls2")
    public ModelAndView exportXls2(Plan4Vo plan4Vo) {
        List<Plan4Vo> list = plan4Service.exportFeedbackSummary(plan4Vo);
        list.forEach(item -> {
            item.setBuildTime(plan4Vo.getBeginTime() + " —— " + plan4Vo.getEndTime());//起始日期
            item.setDecommissioningDate(plan4Vo.getDecommissioningDate());//退役日期
            item.setItemSlip(plan4Vo.getItemSlip());//交接单号
            item.setDescription(plan4Vo.getDescription());//情况说明
            item.setRemark(plan4Vo.getRemark());//备注
        });
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.CLASS, Plan4Vo.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams());
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

    /**
     * 通过excel导入数据
     */
    @Transactional
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        int in = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Plan4Im> list = ExcelImportUtil.importExcel(file.getInputStream(), Plan4Im.class, params);
                Plan4 plan4 = new Plan4();
                for (Plan4Im plan4Im : list) {

                    //判断对象中属性值是否为空
                    boolean flag = (StrUtil.isNotBlank(plan4Im.getEngName()) || StrUtil.isNotBlank(plan4Im.getProjectNo())
                            || StrUtil.isNotBlank(plan4Im.getCableName()) || StrUtil.isNotBlank(plan4Im.getCableCross())
                            || StrUtil.isNotBlank(plan4Im.getVoltageGrade()));
                    if (!flag) return Result.error("请补全电缆信息！");

                    // 属性拷贝 将 plan4Im 接收的excel数据转换为 plan4 实体类数据
                    BeanUtils.copyProperties(plan4Im, plan4);
                    plan4.setPlanType("电缆");
                    plan4.setSendOrdersState(0);
                    plan4.setCompleteState(0);
                    plan4.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
                    plan4.setCreateBy(SysUserConstant.SYS_USER.getUsername());
                    plan4.setUpdateTime(new Date());
                    plan4.setCreateTime(new Date());
                    plan4Service.save(plan4);
                    in++;
                }
                if (in != 0) return Result.ok("文件导入成功！数据行数：" + in);
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

    /**
     * 计划表4配变电统计
     */
    @GetMapping(value = "/selectCable")
    public Result<?> selectCable(@RequestParam(name = "voltageGrade", required = false) String voltageGrade,
                                 @RequestParam(name = "beginTime", required = false) String beginTime,
                                 @RequestParam(name = "endTime", required = false) String endTime,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Plan4Vo> page = new Page<>(pageNo, pageSize);
        IPage<Plan4Vo> pageList = plan4Service.selectCable(voltageGrade, beginTime, endTime, "电缆", page);
        return Result.ok(pageList);
    }

    /**
     * 导出电缆统计数据 excel
     */
    @RequestMapping(value = "/exportXls3")
    public ModelAndView exportXls3(Plan4ExcelVo plan4ExcelVo) {
        List<Plan4ExcelVo> list = plan4Service.exportPlan3(plan4ExcelVo);
        Plan4ExcelVo plan4ExcelVo1 = new Plan4ExcelVo();
        BigDecimal length = BigDecimal.valueOf(0);
        BigDecimal weight = BigDecimal.valueOf(0);
        BigDecimal retrieval_weight = BigDecimal.valueOf(0);
        StringBuilder proName = new StringBuilder();
        for (Plan4ExcelVo excelVo : list) {
            proName.append(excelVo.getEngName()).append(",");
            length = length.add(BigDecimal.valueOf(Double.parseDouble(excelVo.getLength())));
            weight = weight.add(BigDecimal.valueOf(Double.parseDouble(excelVo.getWeight())));
            retrieval_weight = retrieval_weight.add(BigDecimal.valueOf(Double.parseDouble(excelVo.getRetrievalWeight())));
            excelVo.setEngName(null);
        }
        plan4ExcelVo1.setVoltageGrade("合计:");
        plan4ExcelVo1.setLength(length.toString());
        plan4ExcelVo1.setWeight(weight.toString());
        plan4ExcelVo1.setRetrievalWeight(retrieval_weight.toString());
        list.add(plan4ExcelVo1);
        list.get(0).setDecommissioningDate(plan4ExcelVo.getDecommissioningDate());
        list.get(0).setRemark(plan4ExcelVo.getRemark());
        list.get(0).setEngName(proName.toString());
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.CLASS, Plan4ExcelVo.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(plan4ExcelVo.getBeginTime() + "--" + plan4ExcelVo.getEndTime() + "/电缆统计", ""));
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }
}
