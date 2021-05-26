package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.dto.Plan4Im;
import org.jeecg.modules.cable.mapper.Plan4Mapper;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.vo.Plan4ExcelVo;
import org.jeecg.modules.cable.vo.Plan4Vo;
import org.jeecg.modules.cable.vo.SendOrdersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 计划表4
 */
@Service
public class Plan4ServiceImpl extends ServiceImpl<Plan4Mapper, Plan4> implements IPlan4Service {
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IDeliverStorageService deliverStorageService;
    @Autowired
    private IReceivingStorageService receivingStorageService;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IStorageLocationService storageLocationService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IPlan4Service plan4Service;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList) {
        switch (operatorSchema) {
            case "1":
                List<DeliverStorage> deliverStorageList = new LinkedList<DeliverStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 转换泛型集合
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    // 添加入库完单信息
                    DeliverStorage deliverStorage = new DeliverStorage();
                    deliverStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    deliverStorage.setPlanType(4);
                    // 计划4拼接后的物料名称
                    String plan4MaterialName = map.get("recyclingSpecifications").toString().concat(" ").concat(map.get("texture").toString());
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", plan4MaterialName));
                    deliverStorage.setMaterialId(material == null ? 0 : material.getId());
                    deliverStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString()));
                    deliverStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString()));
                    deliverStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    deliverStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    deliverStorage.setAccomplishWeight(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString())));
                    deliverStorage.setAccomplishWeightUnit(2);//电缆重量默认吨
                    deliverStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    deliverStorage.setRecyclingSpecifications(map.get("recyclingSpecifications").toString());
                    deliverStorage.setTexture(map.get("texture").toString());
                    deliverStorage.setSceneSituation(Integer.parseInt(map.get("sceneSituation").toString()));
                    if (null != map.get("anomalousCause").toString()) {
                        deliverStorage.setAnomalousCause(map.get("anomalousCause").toString()); // 异常原因
                    }
                    if (map.get("scenePhotos1") != null) { // 判断是否上传了多张异常图片
                        LinkedHashMap<String, Object> photos1 = (LinkedHashMap<String, Object>) map.get("scenePhotos1");
                        deliverStorage.setScenePhotos(photos1.get("path").toString()); // 添加异常图片1
                        if (map.get("scenePhotos2") != null) {
                            LinkedHashMap<String, Object> photos2 = (LinkedHashMap<String, Object>) map.get("scenePhotos2");
                            deliverStorage.setScenePhotos(photos1.get("path").toString() + "," + photos2.get("path").toString()); // 添加异常图片2
                        }
                    }
                    deliverStorage.setReceiptNo(receiptNo);
                    deliverStorage.setState(1);
                    deliverStorage.setReceiptPhotos(receiptPhotos);
                    deliverStorage.setDeliverTime(DateUtil.parse(taskTime));
                    deliverStorage.setAnnotation(map.get("annotation").toString());
                    deliverStorage.setCreateTime(new Date());
                    deliverStorage.setCreateBy(SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername());
                    deliverStorageList.add(deliverStorage);

                    // 向库存表中存数据
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", Integer.parseInt(map.get("warehouseId").toString()));
                    wrapper.eq("storage_location_id", Integer.parseInt(map.get("storageLocationId").toString()));
                    Plan4 plan4 = this.getOne(new QueryWrapper<Plan4>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan4.getProjectNo());
                    wrapper.eq("material_id", material == null ? null : material.getId());
                    wrapper.eq("recycling_specifications", plan4MaterialName);
                    wrapper.eq("backup1", ids.get(i));
                    wrapper.eq("backup2", 4);
                    if (plan4.getAlreadyDeliverStorage() == null)
                        plan4.setAlreadyDeliverStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString())));
                    else
                        plan4.setAlreadyDeliverStorage(plan4.getAlreadyDeliverStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString()))));
                    plan4Service.updateById(plan4);

                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        // 存在库存,在原本的库存数上增加入库数量即可
                        inventory.setInventoryQuantity(inventory.getInventoryQuantity().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                        if (inventory.getBackup4() != null)   // 库存容积操作
                            inventory.setBackup4(inventory.getBackup4().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                        else
                            inventory.setBackup4(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));

                        if (inventory.getBackup5() != null)   // 库存电缆重量操作
                            inventory.setBackup5(inventory.getBackup5().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString()))));
                        else
                            inventory.setBackup5(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString())));
                        inventoryService.updateById(inventory);
                    } else {
                        // 没有库存信息,需要新增一条库存信息,库存数就等于入库数量即可
                        Inventory entity = new Inventory(Integer.parseInt(map.get("warehouseId").toString()), Integer.parseInt(map.get("storageLocationId").toString()), plan4.getProjectNo(), plan4.getEngName(), material == null ? null : material.getId(), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())), new Date(), SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername(), Integer.parseInt(ids.get(i).toString()), 4, Integer.parseInt(map.get("unit").toString()), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString())), null);
                        entity.setRecyclingSpecifications(plan4MaterialName);
                        inventoryService.save(entity);
                    }
                    StorageLocation storageLocation = storageLocationService.getById(Integer.parseInt(map.get("storageLocationId").toString()));
                    if (storageLocation.getTheCurrentVolume() == null)
                        storageLocation.setTheCurrentVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    else
                        storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                    storageLocationService.updateById(storageLocation);
                }
                deliverStorageService.saveBatch(deliverStorageList);
                return Result.ok("入库完单成功");
            case "0":
                List<ReceivingStorage> receivingStorageList = new LinkedList<ReceivingStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 转换泛型集合
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    // 添加出库信息
                    ReceivingStorage receivingStorage = new ReceivingStorage();
                    receivingStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    receivingStorage.setPlanType(4);
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("cableCross")));
                    receivingStorage.setMaterialId(material == null ? 0 : material.getId());
                    Warehouse warehouse = warehouseService.getOne(new QueryWrapper<Warehouse>().eq("name", map.get("warehouseName").toString()));
                    receivingStorage.setWarehouseId(warehouse == null ? null : warehouse.getId()); // 仓库id
                    StorageLocation storageLocation = storageLocationService.getOne(new QueryWrapper<StorageLocation>().eq("warehouse_id", warehouse.getId()).eq("storage_location_name", map.get("storageLocationName").toString()));
                    receivingStorage.setStorageLocationId(storageLocation == null ? null : storageLocation.getId()); // 库位id
                    receivingStorage.setBackup1(Integer.parseInt(map.get("endWarehouseId").toString())); // 终点仓库id
                    if (map.containsKey("accomplishNum") && StringUtils.isNotBlank(map.get("accomplishNum").toString())) {
                        receivingStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    }
                    if (map.containsKey("unit") && StringUtils.isNotBlank(map.get("unit").toString())) {
                        receivingStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    }
                    receivingStorage.setAccomplishWeight(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString())));
                    receivingStorage.setAccomplishWeightUnit(2);//电缆重量默认吨
                    receivingStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    receivingStorage.setRecyclingSpecifications(map.get("cableCross").toString().split(" ")[0] + map.get("cableCross").toString().split(" ")[1]);
                    receivingStorage.setTexture(map.get("cableCross").toString().split(" ")[2]);
                    receivingStorage.setReceiptNo(receiptNo);
                    receivingStorage.setState(1);
                    receivingStorage.setReceiptPhotos(receiptPhotos);
                    receivingStorage.setReceivingTime(DateUtil.parse(taskTime));
                    receivingStorage.setAnnotation(map.get("annotation").toString()); // 说明
                    receivingStorage.setCreateTime(new Date());
                    receivingStorage.setCreateBy(SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername());
                    receivingStorageList.add(receivingStorage);

                    // 根据仓库、库位、项目编号、物料编号、资产编号查询此库存是否存在
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", warehouse.getId()); // 仓库id
                    wrapper.eq("storage_location_id", storageLocation == null ? null : storageLocation.getId()); // 库位id
                    Plan4 plan4 = this.getOne(new QueryWrapper<Plan4>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan4.getProjectNo());
                    wrapper.eq("material_id", material != null ? material.getId() : null);
                    wrapper.eq("recycling_specifications", map.get("cableCross"));
                    wrapper.eq("backup1", ids.get(i));
                    wrapper.eq("backup2", 4);
                    if (StringUtils.isNotBlank(map.get("accomplishWeight").toString())) {
                        if (plan4.getAlreadyReceivingStorage() == null)
                            plan4.setAlreadyReceivingStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString())));
                        else
                            plan4.setAlreadyReceivingStorage(plan4.getAlreadyReceivingStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString()))));
                        plan4Service.updateById(plan4);
                    }
                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        if (inventory.getInventoryQuantity() == null || inventory.getBackup4() == null || inventory.getBackup5() == null) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("该库存数量或该库存容积或该库存电缆重量不存在,无法进行出库操作");
                        }
                        int result2 = inventory.getBackup4().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        int result3 = inventory.getBackup5().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString())));
                        if (result3 == 0 || result3 > 0) {
                            // 减去库存重量
                            inventory.setBackup5(inventory.getBackup5().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishWeight").toString()))));
                            if (result2 == 0 || result2 > 0) {
                                // 减去库存容积
                                inventory.setBackup4(inventory.getBackup4().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                                // 前端是否输入出库数量
                                if (StringUtils.isNotBlank(map.get("accomplishNum").toString())) {
                                    int result = inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                                    if (result == 0 || result > 0) {
                                        // 减去库存数量
                                        inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                                    } else {
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                                        return Result.error("库存数量不足,无法进行出库操作");
                                    }
                                }
                                inventoryService.updateById(inventory);
                                if (inventory.getBackup5().compareTo(BigDecimal.valueOf(0)) == 0)
                                    inventoryService.removeById(inventory.getId());
                            } else {
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                                return Result.error("库存容积不足,无法进行出库操作");
                            }
                        } else {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("库存电缆重量不足,无法进行出库操作");
                        }
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                        return Result.error("此库存并不存在, 无法进行出库完单操作");
                    }
                    if (storageLocation != null) {
                        int result = storageLocation.getTheCurrentVolume().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        if (result == 0 || result > 0) {
                            storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                            storageLocationService.updateById(storageLocation);
                        } else {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("当前库存容积不足, 无法进行出库完单操作");
                        }
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                        return Result.error("此库存容积并不存在, 无法进行出库完单操作");
                    }
                }
                receivingStorageService.saveBatch(receivingStorageList);
                return Result.ok("出库完单成功");
            default:
                throw new JeecgBootException("完单操作失败~");
        }
    }

    @Override
    public List<Plan4Vo> getPlan4ReceivingStorageList(List<Serializable> ids) {
        return baseMapper.getPlan4ReceivingStorageList(ids);
    }

    @Override
    public List<Plan4> getPlan4DeliverStorage(List<Serializable> ids) {
        return baseMapper.getPlan4DeliverStorage(ids);
    }

    @Override
    public List<Plan4> idsqueryRuList(List<String> ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public List<SendOrdersVo> idsqueryChuList(List<String> ids) {
        return baseMapper.idsqueryChuList(ids);
    }

    @Override
    public IPage<Plan4> pageList(Plan4 plan4, Page<Plan4> page) {
        return page.setRecords(baseMapper.pageList(plan4, page));
    }

    @Override
    public List<Plan4Im> exportPlan4(Plan4 plan4, String explain, String beginTime, String endTime) {
        List<Plan4Im> list = baseMapper.exportPlan4(plan4, beginTime, endTime);
        for (Plan4Im plan4Im : list) {
            plan4Im.setFeedback(explain); // 设置反馈说明
        }
        return list;
    }

    @Override
    public List<Plan4Vo> exportFeedbackSummary(Plan4Vo plan4Vo) {
        return baseMapper.exportFeedbackSummary(plan4Vo);
    }

    @Override
    public IPage<Plan4Vo> selectCable(String voltageGrade, String beginTime, String endTime, String planType, Page<Plan4Vo> page) {
        return page.setRecords(baseMapper.selectCable(voltageGrade, beginTime, endTime, planType, page));
    }

    @Override
    public List<Plan4ExcelVo> exportPlan3(Plan4ExcelVo plan4ExcelVo) {
        return baseMapper.exportPlan3(plan4ExcelVo);
    }

}
