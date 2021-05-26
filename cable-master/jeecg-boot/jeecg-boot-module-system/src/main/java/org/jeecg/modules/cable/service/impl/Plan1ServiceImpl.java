package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SysUserConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.dto.Plan1Im;
import org.jeecg.modules.cable.mapper.Plan1Mapper;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.vo.*;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
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
 * 计划表1
 */
@Service
public class Plan1ServiceImpl extends ServiceImpl<Plan1Mapper, Plan1> implements IPlan1Service {
    @Autowired
    private ISysDictItemService sysDictItemService;
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
    private IPlan1Service plan1Service;

    @Override
    public List<Plan1> idsqueryRuList(List<String> ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public List<SendOrdersVo> idsqueryChuList(List<String> ids) {
        return baseMapper.idsqueryChuList(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> consolidationCompleted(List<Serializable> plan1Ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList) {
        switch (operatorSchema) {
            case "1":
                List<DeliverStorage> deliverStorageList = new LinkedList<DeliverStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 将泛型集合转换
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    DeliverStorage deliverStorage = new DeliverStorage();
                    deliverStorage.setPlanId(Integer.parseInt(plan1Ids.get(i).toString())); // 计划id
                    deliverStorage.setPlanType(1); // 计划类型(1\2\3\4表)
                    // 拿到物料描述去查询物料信息,取出物料id来使用
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("wasteMaterialText").toString()));
                    deliverStorage.setMaterialId(material == null ? 0 : material.getId()); // 物料id
                    deliverStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString())); // 仓库id
                    deliverStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString())); // 库位id
                    deliverStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))); // 完单数量
                    deliverStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString())); // 完单数量单位
                    deliverStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))); // 完单容积
                    deliverStorage.setSceneSituation(Integer.parseInt(map.get("sceneSituation").toString())); // 是否异常
                    deliverStorage.setAnomalousCause(map.get("anomalousCause").toString()); // 异常原因
                    if (map.get("scenePhotos1") != null) { // 判断是否上传了多张异常图片
                        LinkedHashMap<String, Object> photos1 = (LinkedHashMap<String, Object>) map.get("scenePhotos1");
                        deliverStorage.setScenePhotos(photos1.get("path").toString()); // 添加异常图片1
                        if (map.get("scenePhotos2") != null) {
                            LinkedHashMap<String, Object> photos2 = (LinkedHashMap<String, Object>) map.get("scenePhotos2");
                            deliverStorage.setScenePhotos(photos1.get("path").toString() + "," + photos2.get("path").toString()); // 添加异常图片2
                        }
                    }
                    deliverStorage.setReceiptNo(receiptNo); // 交接单号
                    deliverStorage.setState(1); // 完单状态 默认已完成
                    deliverStorage.setReceiptPhotos(receiptPhotos); // 回单照片路径
                    deliverStorage.setDeliverTime(DateUtil.parse(taskTime)); // 入库日期
                    deliverStorage.setAnnotation(map.get("annotation").toString()); // 说明
                    deliverStorage.setCreateTime(new Date()); // 创建时间
                    deliverStorage.setCreateBy(SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername()); // 创建人
                    deliverStorageList.add(deliverStorage);

                    // 向库存表中存数据
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", Integer.parseInt(map.get("warehouseId").toString())); // 仓库id
                    wrapper.eq("storage_location_id", Integer.parseInt(map.get("storageLocationId").toString())); // 库位id
                    Plan1 plan1 = this.getOne(new QueryWrapper<Plan1>().eq("id", plan1Ids.get(i).toString()));
                    wrapper.eq("project_no", plan1.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material == null ? null : material.getId()); // 物料编号
                    wrapper.eq("asset_no", plan1.getAssetNo()); // 资产编号
                    wrapper.eq("backup1", plan1Ids.get(i)); // 计划 Id
                    wrapper.eq("backup2", 1);  // 计划表1
                    if (plan1.getAlreadyDeliverStorage() == null)
                        plan1.setAlreadyDeliverStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    else
                        plan1.setAlreadyDeliverStorage(plan1.getAlreadyDeliverStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())))); // 已入库数
                    plan1Service.updateById(plan1);

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
                        Inventory entity = new Inventory(Integer.parseInt(map.get("warehouseId").toString()), Integer.parseInt(map.get("storageLocationId").toString()), plan1.getProjectNo(), plan1.getProjectName(), material == null ? null : material.getId(), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())), new Date(), SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername(), Integer.parseInt(plan1Ids.get(i).toString()), 1, Integer.parseInt(map.get("unit").toString()), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())), null, map.get("assetNo").toString());
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
                    // 将泛型集合转换
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    ReceivingStorage receivingStorage = new ReceivingStorage();
                    receivingStorage.setPlanId(Integer.parseInt(plan1Ids.get(i).toString())); // 计划id
                    receivingStorage.setPlanType(1); // 计划类型(1\2\3\4表)
                    // 拿到物料描述去查询物料信息,取出物料id来使用
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("wasteMaterialText").toString()));
                    receivingStorage.setMaterialId(material == null ? 0 : material.getId()); // 物料id
                    Warehouse warehouse = warehouseService.getOne(new QueryWrapper<Warehouse>().eq("name", map.get("warehouseName").toString()));
                    receivingStorage.setWarehouseId(warehouse == null ? null : warehouse.getId()); // 仓库id
                    StorageLocation storageLocation = storageLocationService.getOne(new QueryWrapper<StorageLocation>().eq("warehouse_id", warehouse.getId()).eq("storage_location_name", map.get("storageLocationName").toString()));
                    receivingStorage.setStorageLocationId(storageLocation == null ? null : storageLocation.getId()); // 库位id
                    receivingStorage.setBackup1(Integer.parseInt(map.get("endWarehouseId").toString())); // 终点仓库id
                    receivingStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))); // 出库完单数量
                    receivingStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString())); // 出库完单数量单位
                    receivingStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    receivingStorage.setReceiptNo(receiptNo); // 交接单号
                    receivingStorage.setState(1); // 完单状态 默认已完成
                    receivingStorage.setReceiptPhotos(receiptPhotos); // 回单图片路径
                    receivingStorage.setReceivingTime(DateUtil.parse(taskTime)); // 出库日期
                    receivingStorage.setAnnotation(map.get("annotation").toString()); // 说明
                    receivingStorage.setCreateTime(new Date()); // 创建时间
                    receivingStorage.setCreateBy(SysUserConstant.SYS_USER == null ? "无" : SysUserConstant.SYS_USER.getUsername()); // 创建人
                    receivingStorageList.add(receivingStorage);

                    // 根据仓库、库位、项目编号、物料编号、资产编号查询此库存是否存在
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", warehouse.getId()); // 仓库id
                    wrapper.eq("storage_location_id", storageLocation == null ? null : storageLocation.getId()); // 库位id
                    Plan1 plan1 = this.getOne(new QueryWrapper<Plan1>().eq("id", plan1Ids.get(i).toString()));
                    wrapper.eq("project_no", plan1.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material == null ? null : material.getId()); // 物料编号
                    wrapper.eq("asset_no", plan1.getAssetNo()); // 资产编号
                    if (plan1.getAlreadyReceivingStorage() == null)
                        plan1.setAlreadyReceivingStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    else
                        plan1.setAlreadyReceivingStorage(plan1.getAlreadyReceivingStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())))); //TODO 已出库数
                    plan1Service.updateById(plan1);

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
                                if (inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                                    inventoryService.removeById(inventory.getId());
                                }
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
    public List<Plan1Vo> getPlan1ReceivingStorageList(List<Serializable> ids) {
        return baseMapper.getPlan1ReceivingStorageList(ids);
    }

    @Override
    public List<Plan1> getPlan1DeliverStorage(List<Serializable> ids) {
        return baseMapper.getPlan1DeliverStorage(ids);
    }

    @Override
    public IPage<Plan1> pageList(Plan1Vo plan1Vo, Page<Plan1> page) {
        return page.setRecords(baseMapper.pageList(plan1Vo, page));
    }

    @Override
    public List<Plan1Im> exportPlan1(Plan1Im plan1Im, String explain) {
        // 单位之间的转换
        List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
        List<Plan1Im> list = baseMapper.exportPlan1(plan1Im);
        for (Plan1Im p : list) {
            for (SysDictItem dictItem : dictItems) {
                // 导出时判断单位是否存在，存在则进行单位转换
                if (StrUtil.isNotBlank(p.getRawMaterialUnit())) {
                    if (p.getRawMaterialUnit().equals(dictItem.getItemValue())) {
                        p.setRawMaterialUnit(dictItem.getItemText());
                    }
                }
                if (StrUtil.isNotBlank(p.getWasteMaterialUnit())) {
                    if (p.getWasteMaterialUnit().equals(dictItem.getItemValue())) {
                        p.setWasteMaterialUnit(dictItem.getItemText());
                    }
                }
            }
            p.setFeedbackDate(new Date());// 设置反馈日期
            p.setExplain(explain);// 设置反馈说明
        }
        return list;
    }

    @Override
    public IPage<SettleAccountsVo> selectSettleAccounts(String backup1, String planType, String projectNo, Page<SettleAccountsVo> page) {
        return page.setRecords(baseMapper.selectSettleAccounts(backup1, planType, projectNo, page));
    }

    @Override
    public IPage<SettleAccountsDetailsVo> selectSettleAccountsDetails(String projectNo, Page<SettleAccountsDetailsVo> page) {
        return page.setRecords(baseMapper.selectSettleAccountsDetails(projectNo, page));
    }

    @Override
    public IPage<Plan1Vo> selectSubstation(String wasteMaterialText, String beginTime, String endTime, String planType, Page<Plan1Vo> page) {
        return page.setRecords(baseMapper.selectSubstation(wasteMaterialText, beginTime, endTime, planType, page));
    }

    @Override
    public List<Plan1ExcelVo> exportPlan2(Plan1ExcelVo plan1ExcelVo) {
        return baseMapper.exportPlan2(plan1ExcelVo);
    }

    @Override
    public List<IndexXLTJVo> getXLTJList(IndexXLTJVo vo) {
        return baseMapper.getXLTJList(vo);
    }
}
