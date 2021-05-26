package org.jeecg.modules.cable.controller;

import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.vo.PlanVo;
import org.jeecg.modules.cable.vo.SendOrdersTaskVo;
import org.jeecg.modules.cable.vo.SendOrdersVo;
import org.jeecg.modules.cable.vo.TaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * 派单表
 */
@Api(tags = "派单表")
@RestController
@RequestMapping("/cable/sendOrders")
public class SendOrdersController extends JeecgController<SendOrders, ISendOrdersService> {
    @Autowired
    private ISendOrdersService sendOrdersService;
    @Autowired
    private ISendOrdersSubtabulationService sendOrdersSubtabulationService;
    @Autowired
    private IReceivingStorageService receivingStorageService;
    @Autowired
    private IDeliverStorageService deliverStorageService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IPlan1Service plan1Service;
    @Autowired
    private IPlan2Service plan2Service;
    @Autowired
    private IPlan3Service plan3Service;
    @Autowired
    private IPlan4Service plan4Service;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IStorageLocationService storageLocationService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "派单表-分页列表查询")
    @ApiOperation(value = "派单表-分页列表查询", notes = "派单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SendOrders sendOrders,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SendOrders> queryWrapper = QueryGenerator.initQueryWrapper(sendOrders, req.getParameterMap());
        Page<SendOrders> page = new Page<SendOrders>(pageNo, pageSize);
        IPage<SendOrders> pageList = sendOrdersService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 合并派单添加
     */
    @Transactional
    @AutoLog(value = "派单表-合并添加")
    @ApiOperation(value = "派单表-合并添加", notes = "派单表-合并添加")
    @PostMapping(value = "/mergePlanadd")
    public Result<?> MergePlan(@RequestBody SendOrdersVo sendOrdersVo) {
        sendOrdersService.saveMain(sendOrdersVo, sendOrdersVo.getJeecgOrderCustomerList(), sendOrdersVo.getJeecgOrderTicketList());
        return Result.ok("派单成功！");
    }

    /**
     * 派单编辑
     */
    @Transactional
    @AutoLog(value = "派单表-派单编辑")
    @ApiOperation(value = "派单表-派单编辑", notes = "派单表-派单编辑")
    @PostMapping(value = "/mergePlanEdit")
    public Result<?> MergePlanEdit(@RequestBody SendOrdersVo sendOrdersVo) {
        sendOrdersService.MergePlanEdit(sendOrdersVo, sendOrdersVo.getJeecgOrderCustomerList(), sendOrdersVo.getJeecgOrderTicketList());
        return Result.ok("派单编辑成功！");
    }

    /**
     * 添加
     */
    @Transactional
    @AutoLog(value = "派单表-添加")
    @ApiOperation(value = "派单表-添加", notes = "派单表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        SendOrdersVo sendOrdersVo = JSON.parseObject(jsonObject.toJSONString(), SendOrdersVo.class);
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("serial", sendOrdersVo.getSerial());
        List<Material> materialList = materialService.list(queryWrapper);
        if (materialList.size() > 0) {
            Integer id = sendOrdersService.saveSendOrdersVo(sendOrdersVo, new Date(), SysUserConstant.SYS_USER.getUsername());
            if (sendOrdersVo.getLicense() != null) {
                for (String s : sendOrdersVo.getLicense()) {
                    sendOrdersSubtabulationService.saveSendOrdersSubtabulation(id, 0, s, sendOrdersVo.getTaskTime());
                }
            }
            if (sendOrdersVo.getRealname() != null) {
                for (String s : sendOrdersVo.getRealname()) {
                    sendOrdersSubtabulationService.saveSendOrdersSubtabulation(id, 1, s, sendOrdersVo.getTaskTime());
                }
            }
            sendOrdersService.updatePlanState(sendOrdersVo.getPlanId(), sendOrdersVo.getPlanType());
            return Result.ok("派单成功！");
        }
        return Result.error("该计划物料不存在！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "派单表-编辑")
    @ApiOperation(value = "派单表-编辑", notes = "派单表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SendOrdersVo sendOrdersVo) {
        SendOrders sendOrders = new SendOrders();
        sendOrders.setId(sendOrdersVo.getId());
        sendOrders.setBackup3(sendOrdersVo.getLinkman());
        sendOrders.setBackup2(sendOrdersVo.getAddress());
        sendOrders.setBackup4(sendOrdersVo.getPhone());
        sendOrdersService.updateById(sendOrders);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @Transactional
    @AutoLog(value = "派单表-通过id删除")
    @ApiOperation(value = "派单表-通过id删除", notes = "派单表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        sendOrdersService.removeSendOrders(id);
        return Result.ok("删除成功!");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "派单表-通过id查询")
    @ApiOperation(value = "派单表-通过id查询", notes = "派单表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        SendOrders sendOrders = sendOrdersService.getById(id);
        if (sendOrders == null) return Result.error("未找到对应数据");
        return Result.ok(sendOrders);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SendOrders sendOrders) {
        return super.exportXls(request, sendOrders, SendOrders.class, "派单表");
    }

    /**
     * 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SendOrders.class);
    }

    /**
     * 根据年份和月份查询当月每日的车辆任务的数量
     */
    @GetMapping(value = "/selectTheSameMonthSendOrders")
    public Result<?> selectTheSameMonthVehicleTask(String date) {
        List<TaskVo> list = sendOrdersService.selectTheSameMonthSendOrders(date);
        return Result.ok(list);
    }

    /**
     * 根据日期查询当天车辆任务信息
     */
    @GetMapping(value = "/taskList")
    public Result<?> taskList(String date,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SendOrdersTaskVo> page = new Page<SendOrdersTaskVo>(pageNo, pageSize);
        IPage<SendOrdersTaskVo> list = sendOrdersService.taskList(date, page);
        return Result.ok(list);
    }

    /**
     * 返回近5年年份
     */
    @GetMapping(value = "/yearsList")
    public Result<?> yearsList() {
        List<String> list = sendOrdersService.yearsList();
        return Result.ok(list);
    }

    /**
     * 根据项目编号查询入库出库完单信息
     */
    @GetMapping(value = "/selectPlanAccomplish")
    public Result<?> selectPlanAccomplish(@RequestParam(name = "projectNo", required = true) String projectNo,
                                          @RequestParam(name = "planId", required = true) String planId,
                                          @RequestParam(name = "planType", required = true) String planType,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "sendOrdersId", required = true) String sendOrdersId) {
        if (sendOrdersId.equals("www")) {
            sendOrdersId = null;
        }
        Page<PlanVo> page = new Page<>(pageNo, pageSize);
        List<PlanVo> list = sendOrdersService.selectPlan2Accomplish(projectNo, planId, planType, sendOrdersId, page);
        return Result.ok(list);
    }

    /**
     * 完单操作
     */
    @PutMapping(value = "/planedit")
    public Result<?> planedit(@RequestBody JSONObject jsonObject) {
        PlanVo planVo = JSON.parseObject(jsonObject.toJSONString(), PlanVo.class);
        Result<?> planedit = sendOrdersService.planedit(planVo);
        if (planedit.getCode() == 200) return Result.ok(planedit.getMessage());
        if (planedit.getCode() == 500) return Result.error(planedit.getMessage());
        return Result.ok(planedit);
    }

    /**
     * 根据计划id和计划类型查询历史派单记录
     */
    @GetMapping(value = "/selectSendOrdersController")
    public Result<?> selectSendOrdersController(@RequestParam(name = "ids", required = true) String ids,
                                                @RequestParam(name = "planType", required = true) String planType,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SendOrdersVo> page = new Page<>(pageNo, pageSize);
        IPage<SendOrdersVo> iPage = sendOrdersService.selectSendOrdersController(ids, planType, page);
        return Result.ok(iPage);
    }

    /**
     * 查询当天所有的派单[今日派单]
     */
    @GetMapping(value = "/selectPlanSendOrdersTheSameDay")
    public Result<?> selectPlanSendOrdersTheSameDay(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SendOrdersVo> page = new Page<>(pageNo, pageSize);
        IPage<SendOrdersVo> iPage = sendOrdersService.selectPlanSendOrdersTheSameDay(page);
        return Result.ok(iPage);
    }

    /**
     * 查询完单记录
     */
    @GetMapping(value = "/selectSendOrdersWD")
    public Result<?> selectSendOrdersWD(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "ids", required = true) String ids,
                                        @RequestParam(name = "planType", required = true) String planType,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SendOrdersVo> page = new Page<>(pageNo, pageSize);
        IPage<SendOrdersVo> iPage = sendOrdersService.selectSendOrdersWD(ids, planType, page);
        return Result.ok(iPage);
    }

    /**
     * 通过id删除完单记录
     */
    @PutMapping(value = "/wddelete/{tableId}")
    public Result<?> wdDelete(@RequestBody SendOrdersVo sendOrdersVo, @PathVariable(name = "tableId") String tableId) {
        return sendOrdersService.deletelStoragesById(sendOrdersVo.getId(), sendOrdersVo.getPlanType(), sendOrdersVo, tableId);
    }

    /**
     * 完单记录编辑
     */
    @AutoLog(value = "完单表-编辑")
    @ApiOperation(value = "完单表-编辑", notes = "完单表-编辑")
    @PutMapping(value = "/wdedit")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> wdedit(@RequestBody SendOrdersVo sendOrdersVo) {
        Material material;
        if (sendOrdersVo.getPlanType().equals("出库")) {
            ReceivingStorage receivingStorage = new ReceivingStorage();
            SendOrdersVo sendOrdersVo1 = sendOrdersVo.getCompleteOrderList().get(0);
            receivingStorage.setId(sendOrdersVo.getId());//出库表ID
            receivingStorage.setReceivingTime(sendOrdersVo.getTaskTime());//出库日期
            receivingStorage.setReceiptNo(sendOrdersVo.getReceiptNo());//交接单号
            receivingStorage.setReceiptPhotos(sendOrdersVo.getReceiptPhotos());//回单照片
            receivingStorage.setAccomplishNum(sendOrdersVo1.getAccomplishNum());//完单数量
            receivingStorage.setAccomplishNumUnit(sendOrdersVo1.getUnit());//完单数量单位
            receivingStorage.setAccomplishVolume(sendOrdersVo1.getAccomplishVolume());//容积
            receivingStorage.setWarehouseId(sendOrdersVo1.getWarehouseId());//自家仓库
            receivingStorage.setStorageLocationId(sendOrdersVo1.getStorageLocationId());//库位
            receivingStorage.setBackup1(sendOrdersVo1.getEndWarehouseId());//目标仓库
            receivingStorage.setAnnotation(sendOrdersVo1.getAnnotation());//情况说明
            //计划四（材质和规格）
            if (sendOrdersVo.getPtype().equals("4")) {
                receivingStorage.setRecyclingSpecifications(sendOrdersVo1.getRawMaterialText());
                receivingStorage.setTexture(sendOrdersVo1.getTexture());
            }
            receivingStorageService.updateById(receivingStorage);

            // 拿到物料描述去查询物料信息,取出物料id来使用
            material = materialService.getOne(new QueryWrapper<Material>().eq("name", sendOrdersVo.getRawMaterialText() + " " + sendOrdersVo.getTexture()));
            if (null == material) return Result.error("没有查到该物料!");

            // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
            QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
            wrapper.eq("warehouse_id", sendOrdersVo.getZjid()); // 自家仓库id
            wrapper.eq("storage_location_id", sendOrdersVo.getKwid()); // 库位id
            switch (sendOrdersVo.getPtype()) {
                case "1":
                    Plan1 plan1 = plan1Service.getOne(new QueryWrapper<Plan1>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan1.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("asset_no", plan1.getAssetNo()); // 资产编号
                    wrapper.eq("backup1", sendOrdersVo.getPlanId()); // 计划 Id
                    wrapper.eq("backup2", 1);  // 计划表1
                    if (plan1.getAlreadyReceivingStorage() == null)
                        plan1.setAlreadyReceivingStorage(sendOrdersVo1.getAccomplishNum());
                    else
                        plan1.setAlreadyReceivingStorage(plan1.getAlreadyReceivingStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan1Service.updateById(plan1);
                    break;
                case "2":
                    Plan2 plan2 = plan2Service.getOne(new QueryWrapper<Plan2>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan2.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("asset_no", plan2.getRetiredAssetNo()); // 退役资产编号
                    if (plan2.getAlreadyReceivingStorage() == null)
                        plan2.setAlreadyReceivingStorage(sendOrdersVo1.getAccomplishNum());
                    else
                        plan2.setAlreadyReceivingStorage(plan2.getAlreadyReceivingStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan2Service.updateById(plan2);
                    break;
                case "3":
                    Plan3 plan3 = plan3Service.getOne(new QueryWrapper<Plan3>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan3.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("asset_no", plan3.getProTheorderNo()); // 采购订单号
                    if (plan3.getAlreadyReceivingStorage() == null)
                        plan3.setAlreadyReceivingStorage(sendOrdersVo1.getAccomplishNum());
                    else
                        plan3.setAlreadyReceivingStorage(plan3.getAlreadyReceivingStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan3Service.updateById(plan3);
                    break;
                case "4":
                    Plan4 plan4 = plan4Service.getOne(new QueryWrapper<Plan4>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan4.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("recycling_specifications", sendOrdersVo1.getRawMaterialText() + " " + sendOrdersVo1.getTexture()); // 电缆 =  规格 + ‘ ’ +材质
                    wrapper.eq("backup1", sendOrdersVo.getPlanId()); // 计划 Id
                    wrapper.eq("backup2", 4);  // 计划表 4
                    if (plan4.getAlreadyReceivingStorage() == null)
                        plan4.setAlreadyReceivingStorage(sendOrdersVo1.getAccomplishWeight());
                    else
                        plan4.setAlreadyReceivingStorage(plan4.getAlreadyReceivingStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan4Service.updateById(plan4);
                    break;
                default:
                    throw new JeecgBootException(this.getClass().getName() + "没有对应处理项~");
            }

            Inventory inventory = inventoryService.getOne(wrapper);
            if (inventory == null) return Result.error("没有查到该库存信息!");
            // 存在库存,在原本的库存数上减少数量，目标仓库增加数量
            inventory.setWarehouseId(sendOrdersVo1.getWarehouseId());
            inventory.setStorageLocationId(sendOrdersVo1.getStorageLocationId());
            inventory.setUpdateTime(new Date());
            inventory.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
            inventory.setBackup3(sendOrdersVo1.getUnit());
            inventory.setMaterialId(material.getId());
            if (sendOrdersVo.getPtype().equals("4")) {
                inventory.setMaterialId(materialService.getOne(new QueryWrapper<Material>().eq("name", sendOrdersVo1.getRawMaterialText() + " " + sendOrdersVo1.getTexture())).getId());
                inventory.setBackup5(sendOrdersVo1.getAccomplishWeight());
                inventory.setRecyclingSpecifications(sendOrdersVo1.getRawMaterialText());
            }
            inventory.setInventoryQuantity(inventory.getInventoryQuantity().add(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
            inventory.setBackup4(inventory.getBackup4().add(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getRjchange()))));
            inventoryService.updateById(inventory);

            if (sendOrdersVo1.getStorageLocationId().equals(sendOrdersVo.getKwid())) {
                StorageLocation storageLocation = storageLocationService.getById(sendOrdersVo1.getStorageLocationId());
                if (storageLocation == null) return Result.error("没有查到该库位信息!");
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getRjchange()))));
                storageLocationService.updateById(storageLocation);
            }

            if (!sendOrdersVo1.getStorageLocationId().equals(sendOrdersVo.getKwid())) {
                StorageLocation storageLocation = storageLocationService.getById(sendOrdersVo1.getStorageLocationId());
                if (storageLocation == null) return Result.error("没有查到该库位信息!");
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().add(sendOrdersVo1.getAccomplishVolume()));
                storageLocationService.updateById(storageLocation);
                storageLocation = storageLocationService.getById(sendOrdersVo.getKwid());
                if (storageLocation == null) return Result.error("没有查到该库位信息!");
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(sendOrdersVo1.getAccomplishVolume()));
                storageLocationService.updateById(storageLocation);
            }
        } else if (sendOrdersVo.getPlanType().equals("入库")) {
            SendOrdersVo sendOrdersVo1 = sendOrdersVo.getCompleteOrderList().get(0);
            DeliverStorage deliverStorage = new DeliverStorage();
            deliverStorage.setId(sendOrdersVo.getId());//入口库表Id
            deliverStorage.setDeliverTime(sendOrdersVo.getTaskTime());//出库日期
            deliverStorage.setReceiptNo(sendOrdersVo.getReceiptNo());//交接单号
            deliverStorage.setReceiptPhotos(sendOrdersVo.getReceiptPhotos());//回单照片
            deliverStorage.setAccomplishNum(sendOrdersVo1.getAccomplishNum());//完单数量
            deliverStorage.setAccomplishNumUnit(sendOrdersVo1.getUnit());//完单数量单位
            deliverStorage.setAccomplishVolume(sendOrdersVo1.getAccomplishVolume());//容积
            deliverStorage.setWarehouseId(sendOrdersVo1.getWarehouseId());//目标仓库ID
            deliverStorage.setStorageLocationId(sendOrdersVo1.getStorageLocationId());//库位
            deliverStorage.setSceneSituation(Integer.parseInt(sendOrdersVo1.getSceneSituation()));//是否异常
            deliverStorage.setAnomalousCause(sendOrdersVo1.getAnomalousCause());//异常原因
            deliverStorage.setScenePhotos(sendOrdersVo1.getScenePhotos1());//异常图片
            deliverStorage.setAnnotation(sendOrdersVo1.getAnnotation());//说明
            //计划四（材质和规格）
            if (sendOrdersVo.getPtype().equals("4")) {
                //当编辑计划4 电缆时，更新规格 以及材质
                deliverStorage.setRecyclingSpecifications(sendOrdersVo1.getRawMaterialText());
                deliverStorage.setTexture(sendOrdersVo1.getTexture());
                //当编辑计划4 电缆时，更新重量 以及单位
                deliverStorage.setAccomplishWeight(sendOrdersVo1.getAccomplishWeight());
                deliverStorage.setAccomplishWeightUnit(2);//电缆重量默认吨
            }
            deliverStorageService.updateById(deliverStorage);

            // 查老的库存信息，使用老物料,取物料id来使用
            material = materialService.getOne(new QueryWrapper<Material>().eq("name", sendOrdersVo.getRawMaterialText() + " " + sendOrdersVo.getTexture()));
            if (null == material) return Result.error("没有查到该物料!");
            // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
            QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
            wrapper.eq("warehouse_id", sendOrdersVo.getZjid()); // 仓库id
            wrapper.eq("storage_location_id", sendOrdersVo.getKwid()); // 库位id
            switch (sendOrdersVo.getPtype()) {
                case "1":
                    Plan1 plan1 = plan1Service.getOne(new QueryWrapper<Plan1>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan1.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("asset_no", plan1.getAssetNo()); // 资产编号
                    wrapper.eq("backup1", sendOrdersVo.getPlanId()); // 计划 Id
                    wrapper.eq("backup2", 1);  // 计划表1
                    if (plan1.getAlreadyDeliverStorage() == null)
                        plan1.setAlreadyDeliverStorage(sendOrdersVo1.getAccomplishNum());
                    else
                        plan1.setAlreadyDeliverStorage(plan1.getAlreadyDeliverStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan1Service.updateById(plan1);
                    break;
                case "2":
                    Plan2 plan2 = plan2Service.getOne(new QueryWrapper<Plan2>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan2.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("asset_no", plan2.getRetiredAssetNo()); // 退役资产编号
                    if (plan2.getAlreadyDeliverStorage() == null)
                        plan2.setAlreadyDeliverStorage(sendOrdersVo1.getAccomplishNum());
                    else
                        plan2.setAlreadyDeliverStorage(plan2.getAlreadyDeliverStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan2Service.updateById(plan2);
                    break;
                case "3":
                    Plan3 plan3 = plan3Service.getOne(new QueryWrapper<Plan3>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan3.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("asset_no", plan3.getProTheorderNo()); // 采购订单号
                    if (plan3.getAlreadyDeliverStorage() == null)
                        plan3.setAlreadyDeliverStorage(sendOrdersVo1.getAccomplishNum());
                    else
                        plan3.setAlreadyDeliverStorage(plan3.getAlreadyDeliverStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan3Service.updateById(plan3);
                    break;
                case "4":
                    Plan4 plan4 = plan4Service.getOne(new QueryWrapper<Plan4>().eq("id", sendOrdersVo.getPlanId()));
                    wrapper.eq("project_no", plan4.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料id
                    wrapper.eq("recycling_specifications", sendOrdersVo1.getRawMaterialText() + " " + sendOrdersVo1.getTexture()); // 电缆 =  规格 + ‘ ’ +材质
                    wrapper.eq("backup1", sendOrdersVo.getPlanId()); // 计划 Id
                    wrapper.eq("backup2", 4);  // 计划表 4
                    if (plan4.getAlreadyDeliverStorage() == null)
                        plan4.setAlreadyDeliverStorage(sendOrdersVo1.getAccomplishWeight());
                    else
                        plan4.setAlreadyDeliverStorage(plan4.getAlreadyDeliverStorage().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
                    plan4Service.updateById(plan4);
                    break;
                default:
                    throw new JeecgBootException(this.getClass().getName() + "没有对应处理项~");
            }

            Inventory inventory = inventoryService.getOne(wrapper);
            if (inventory == null) return Result.error("没有查到该库存信息!");
            // 存在库存,在原本的库存数上增加入库数量即可
            inventory.setWarehouseId(sendOrdersVo1.getWarehouseId());
            inventory.setStorageLocationId(sendOrdersVo1.getStorageLocationId());
            inventory.setUpdateTime(new Date());
            inventory.setUpdateBy(SysUserConstant.SYS_USER.getUsername());
            inventory.setBackup3(sendOrdersVo1.getUnit());
            inventory.setMaterialId(material.getId());
            if (sendOrdersVo.getPtype().equals("4")) {
                inventory.setMaterialId(materialService.getOne(new QueryWrapper<Material>().eq("name", sendOrdersVo1.getRawMaterialText() + " " + sendOrdersVo1.getTexture())).getId());
                inventory.setBackup5(sendOrdersVo1.getAccomplishWeight());
                inventory.setRecyclingSpecifications(sendOrdersVo1.getRawMaterialText());
            }
            inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getSlchange()))));
            inventory.setBackup4(inventory.getBackup4().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getRjchange()))));
            inventoryService.updateById(inventory);

            if (sendOrdersVo1.getStorageLocationId().equals(sendOrdersVo.getKwid())) {
                StorageLocation storageLocation = storageLocationService.getById(sendOrdersVo1.getStorageLocationId());
                if (storageLocation == null) return Result.error("没有查到该库位信息!");
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(BigDecimal.valueOf(Double.parseDouble(sendOrdersVo.getRjchange()))));
                storageLocationService.updateById(storageLocation);
            }

            if (!sendOrdersVo1.getStorageLocationId().equals(sendOrdersVo.getKwid())) {
                StorageLocation storageLocation = storageLocationService.getById(sendOrdersVo1.getStorageLocationId());
                if (storageLocation == null) return Result.error("没有查到该库位信息!");
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().add(sendOrdersVo1.getAccomplishVolume()));
                storageLocationService.updateById(storageLocation);
                storageLocation = storageLocationService.getById(sendOrdersVo.getKwid());
                if (storageLocation == null) return Result.error("没有查到该库位信息!");
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(sendOrdersVo1.getAccomplishVolume()));
                storageLocationService.updateById(storageLocation);
            }
        }
        return Result.ok("编辑成功!");
    }

}
