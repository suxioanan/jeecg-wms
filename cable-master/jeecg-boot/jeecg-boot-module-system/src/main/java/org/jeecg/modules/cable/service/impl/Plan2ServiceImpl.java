package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.dto.Plan2Im;
import org.jeecg.modules.cable.mapper.*;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.vo.IndexBPTJVo;
import org.jeecg.modules.cable.vo.Plan2Vo;
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
 * 计划表2
 */
@Service
public class Plan2ServiceImpl extends ServiceImpl<Plan2Mapper, Plan2> implements IPlan2Service {
    @Autowired
    private IDeliverStorageService deliverStorageService;
    @Autowired
    private IReceivingStorageService receivingStorageService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IStorageLocationService storageLocationService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IPlan2Service plan2Service;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList) {
        switch (operatorSchema) {
            case "1":
                List<DeliverStorage> deliverStorageList = new LinkedList<DeliverStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 转换泛型集合
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    // 向入库完单表存数据
                    DeliverStorage deliverStorage = new DeliverStorage();
                    deliverStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    deliverStorage.setPlanType(2);
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("backup2").toString()));
                    deliverStorage.setMaterialId(material == null ? 0 : material.getId());
                    deliverStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString()));
                    deliverStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString()));
                    deliverStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    deliverStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    deliverStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    deliverStorage.setSceneSituation(Integer.parseInt(map.get("sceneSituation").toString()));
                    deliverStorage.setAnomalousCause(map.get("anomalousCause").toString());
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
                    Plan2 plan2 = this.getOne(new QueryWrapper<Plan2>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan2.getProjectNo());
                    wrapper.eq("material_id", material == null ? null : material.getId());
                    wrapper.eq("asset_no", plan2.getRetiredAssetNo());
                    // 向计划表2中添加积累的已入库数量
                    if (plan2.getAlreadyDeliverStorage() == null)
                        plan2.setAlreadyDeliverStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    else
                        plan2.setAlreadyDeliverStorage(plan2.getAlreadyDeliverStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                    plan2Service.updateById(plan2);

                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        // 存在库存,在原本的库存数上增加入库数量即可
                        inventory.setInventoryQuantity(inventory.getInventoryQuantity().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                        if (inventory.getBackup4() != null)
                            inventory.setBackup4(inventory.getBackup4().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                        else
                            inventory.setBackup4(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        inventoryService.updateById(inventory);
                    } else {
                        // 没有库存信息,需要新增一条库存信息,库存数就等于入库数量即可
                        Inventory entity = new Inventory(Integer.parseInt(map.get("warehouseId").toString()), Integer.parseInt(map.get("storageLocationId").toString()), plan2.getProjectNo(), plan2.getSite(), material == null ? null : material.getId(), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())), new Date(), SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername(), Integer.parseInt(ids.get(i).toString()), 2, Integer.parseInt(map.get("unit").toString()), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())), null, map.get("retiredAssetNo").toString());
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
                    // 向出库完单表中添加数据
                    ReceivingStorage receivingStorage = new ReceivingStorage();
                    receivingStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    receivingStorage.setPlanType(2);
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("backup2").toString()));
                    receivingStorage.setMaterialId(material == null ? 0 : material.getId());
                    Warehouse warehouse = warehouseService.getOne(new QueryWrapper<Warehouse>().eq("name", map.get("warehouseName").toString()));
                    receivingStorage.setWarehouseId(warehouse == null ? null : warehouse.getId()); // 仓库id
                    StorageLocation storageLocation = storageLocationService.getOne(new QueryWrapper<StorageLocation>().eq("warehouse_id", warehouse.getId()).eq("storage_location_name", map.get("storageLocationName").toString()));
                    receivingStorage.setStorageLocationId(storageLocation == null ? null : storageLocation.getId()); // 库位id
                    receivingStorage.setBackup1(Integer.parseInt(map.get("endWarehouseId").toString())); // 终点仓库id
                    receivingStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    receivingStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    receivingStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    receivingStorage.setReceiptNo(receiptNo);
                    receivingStorage.setState(1);
                    receivingStorage.setReceiptPhotos(receiptPhotos);
                    receivingStorage.setReceivingTime(DateUtil.parse(taskTime));
                    receivingStorage.setAnnotation(map.get("annotation").toString()); // 说明
                    receivingStorage.setCreateTime(new Date());
                    receivingStorage.setCreateBy(SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername());
                    receivingStorageList.add(receivingStorage);

                    // 向库存表中存数据
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", warehouse.getId()); // 仓库id
                    wrapper.eq("storage_location_id", storageLocation == null ? null : storageLocation.getId()); // 库位id
                    Plan2 plan2 = this.getOne(new QueryWrapper<Plan2>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan2.getProjectNo());
                    wrapper.eq("material_id", material != null ? material.getId() : null);
                    wrapper.eq("asset_no", plan2.getRetiredAssetNo());
                    // 向计划表2中添加积累的已出库数量
                    if (plan2.getAlreadyReceivingStorage() == null)
                        plan2.setAlreadyReceivingStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    else
                        plan2.setAlreadyReceivingStorage(plan2.getAlreadyReceivingStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));

                    plan2Service.updateById(plan2);

                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        if (inventory.getInventoryQuantity() == null || inventory.getBackup4() == null) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("该库存数量或该库存容积不存在,无法进行出库操作");
                        }
                        int result = inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                        int result2 = inventory.getBackup4().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        if (result == 0 || result > 0) {
                            inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                            if (result2 == 0 || result2 > 0) {
                                inventory.setBackup4(inventory.getBackup4().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                                inventoryService.updateById(inventory);
                                if (inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0)
                                    inventoryService.removeById(inventory.getId());
                            } else {
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                                return Result.error("库存容积不足,无法进行出库操作");
                            }
                        } else {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("库存数量不足,无法进行出库操作");
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
    public List<Plan2Vo> getPlan2ReceivingStorageList(List<Serializable> ids) {
        return baseMapper.getPlan2ReceivingStorageList(ids);
    }

    @Override
    public List<Plan2> getPlan2DeliverStorage(List<Serializable> ids) {
        return baseMapper.getPlan2DeliverStorage(ids);
    }

    @Override
    public List<Plan2> idsqueryRuList(List<String> ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public List<SendOrdersVo> idsqueryChuList(List<String> ids) {
        return baseMapper.idsqueryChuList(ids);
    }

    @Override
    public IPage<Plan2> pageList(Plan2 plan2, Page<Plan2> page) {
        return page.setRecords(baseMapper.pageList(plan2, page));
    }

    @Override
    public List<Plan2Im> exportPlan2(Plan2 plan2, String explain) {
        List<Plan2Im> list = baseMapper.exportPlan2(plan2);
        for (Plan2Im plan2Im : list) {
            plan2Im.setFeedbackDateTime(new Date()); // 设置反馈日期
            plan2Im.setAnnotation(explain);// 设置反馈说明
        }
        return list;
    }

    @Override
    public List<IndexBPTJVo> getBPTJList(IndexBPTJVo vo) {
        return baseMapper.getBPTJList(vo);
    }
}
