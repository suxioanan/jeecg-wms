package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.mapper.*;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.vo.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 派单表
 */
@Service
public class SendOrdersServiceImpl extends ServiceImpl<SendOrdersMapper, SendOrders> implements ISendOrdersService {
    @Autowired
    private SendOrdersMapper sendOrdersMapper;
    @Autowired
    private Plan1Mapper plan1Mapper;
    @Autowired
    private SendOrdersSubtabulationMapper sendOrdersSubtabulationMapper;
    @Autowired
    private DeliverStorageMapper deliverStorageMapper;
    @Autowired
    private ReceivingStorageMapper receivingStorageMapper;
    @Autowired
    private StorageLocationMapper storageLocationMapper;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private IPlan1Service plan1Service;
    @Autowired
    private IPlan2Service plan2Service;
    @Autowired
    private IPlan3Service plan3Service;
    @Autowired
    private IPlan4Service plan4Service;
    @Autowired
    private IDeliverStorageService deliverStorageService;
    @Autowired
    private IReceivingStorageService receivingStorageService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IStorageLocationService storageLocationService;
    @Autowired
    private ISysUserService sysUserService;

    @Transactional
    @Override
    public void MergePlanEdit(SendOrdersVo sendOrdersVo, List<SendOrdersVo> sendOrdersMainPageVoList, List<SendOrdersTaskVo> vehicleList) {
        //车辆派单表 关联第一条派单记录 id
        Integer id = null;

        // 遍历所有派单信息，进行派单操作
        for (SendOrdersVo orders : sendOrdersMainPageVoList) {
            SendOrders sendOrders = new SendOrders();
            Integer warehouseId = null;
            Integer storageLocationId = null;
            if (null != orders.getWname())
                warehouseId = warehouseMapper.selectOne(new QueryWrapper<Warehouse>().eq("name", orders.getWname())).getId();
            if (null != orders.getStoragename())
                storageLocationId = storageLocationMapper.selectOne(new QueryWrapper<StorageLocation>().eq("storage_location_name", orders.getStoragename())).getId();

            // 将共有部分派单参数赋值给该计划派单参数
            sendOrders.setOperatorSchema(sendOrdersVo.getOperatorSchema());     //派单类型【出库/入库】
            sendOrders.setTaskTime(sendOrdersVo.getTaskTime());                 //任务时间
            sendOrders.setBackup2(sendOrdersVo.getBackup2());                   //任务地址
            sendOrders.setBackup3(sendOrdersVo.getBackup3());                   //联系人
            sendOrders.setBackup4(sendOrdersVo.getBackup4());                   //电话
            sendOrders.setProjectNo(orders.getProjectNo());                     //工程账号
            sendOrders.setWarehouseId(warehouseId);                             //自家仓库  warehouseId
            sendOrders.setStorageLocationId(storageLocationId);                 //自家库位  storageLocationId
            sendOrders.setEndWarehouseId(orders.getEndWarehouseId());           //终点仓库  endWarehouseId
            sendOrders.setBackup1(orders.getBackup1());                         //派单数量
            sendOrders.setPlanId(orders.getId());                               //计划id
            sendOrders.setId(sendOrdersVo.getId());                             //派单id
            sendOrdersMapper.updateById(sendOrders);

            // 派单成功之后，只获取第一次的派单id
            if (id == null) id = sendOrdersVo.getId();

            // 更新计划 1 表 的派单状态为“已派单”
            Plan1 plan1 = new Plan1();
            plan1.setId(sendOrders.getPlanId());
            plan1.setSendOrdersState(1);
            plan1Mapper.updateById(plan1);
        }

        // 派单编辑车辆和员工时，先根据派单id删除所有员工和车辆，再进行新增
        sendOrdersSubtabulationMapper.delete(new QueryWrapper<SendOrdersSubtabulation>().eq("send_orders_id", id));

        // 派单-车辆-员工关系表添加 “车辆” 数据
        SendOrdersSubtabulation subtabulation = new SendOrdersSubtabulation();
        subtabulation.setSendOrdersId(id);
        subtabulation.setTaskTime(sendOrdersVo.getTaskTime());
        if (vehicleList != null) {
            for (SendOrdersTaskVo s : vehicleList) {
                // 根据车辆数量，新增车辆派单数据个数
                for (int i = 0; i < s.getNumber(); i++) {
                    subtabulation.setDistributionType(0);
                    subtabulation.setTypeId(s.getLicense());
                    sendOrdersSubtabulationMapper.insert(subtabulation);
                }
            }
        }
        if (sendOrdersVo.getRealname() != null) {
            for (String s : sendOrdersVo.getRealname()) {
                // 派单-车辆-员工关系表添加 “员工” 数据
                subtabulation.setDistributionType(1);
                subtabulation.setTypeId(sysUserService.getOne(new QueryWrapper<SysUser>().eq("realname", s)).getId());
                sendOrdersSubtabulationMapper.insert(subtabulation);
            }
        }
    }

    @Transactional
    @Override
    public void saveMain(SendOrdersVo sendOrdersVo, List<SendOrdersVo> sendOrdersMainPageVoList, List<SendOrdersTaskVo> vehicleList) {
        //车辆派单表 关联第一条派单记录 id
        Integer id = null;

        // 遍历所有派单信息，进行派单操作
        for (SendOrdersVo orders : sendOrdersMainPageVoList) {
            SendOrders sendOrders = new SendOrders();
            Integer warehouseId = null;
            Integer storageLocationId = null;
            if (null != orders.getWname())
                warehouseId = warehouseMapper.selectOne(new QueryWrapper<Warehouse>().eq("name", orders.getWname())).getId();
            if (null != orders.getStoragename())
                storageLocationId = storageLocationMapper.selectOne(new QueryWrapper<StorageLocation>().eq("storage_location_name", orders.getStoragename())).getId();

            // 将共有部分派单参数赋值给该计划派单参数
            BeanUtils.copyProperties(sendOrdersVo, sendOrders);              // sendOrdersVo → sendOrders
            sendOrders.setProjectNo(orders.getProjectNo());                 //工程账号
            sendOrders.setWarehouseId(warehouseId);                         //自家仓库  warehouseId
            sendOrders.setStorageLocationId(storageLocationId);             //自家库位  storageLocationId
            sendOrders.setEndWarehouseId(orders.getEndWarehouseId());       //终点仓库  endWarehouseId
            sendOrders.setBackup1(orders.getBackup1());                     //派单数量
            sendOrders.setPlanId(orders.getId());                           //计划id
            sendOrders.setId(null);                                         //派单id 设置为空
            sendOrdersMapper.insert(sendOrders);

            // 派单成功之后，只获取第一次的派单id
            if (id == null) id = sendOrders.getId();

            // 更新计划 1 表 的派单状态为“已派单”
            Plan1 plan1 = new Plan1();
            plan1.setId(sendOrders.getPlanId());
            plan1.setSendOrdersState(1);
            plan1Mapper.updateById(plan1);
        }

        // 派单-车辆-员工关系表添加 “车辆” 数据
        SendOrdersSubtabulation subtabulation = new SendOrdersSubtabulation();
        subtabulation.setSendOrdersId(id);
        subtabulation.setTaskTime(sendOrdersVo.getTaskTime());
        if (vehicleList != null) {
            for (SendOrdersTaskVo s : vehicleList) {
                // 根据车辆数量，新增车辆派单数据个数
                for (int i = 0; i < s.getNumber(); i++) {
                    subtabulation.setDistributionType(0);
                    subtabulation.setTypeId(s.getLicense());
                    sendOrdersSubtabulationMapper.insert(subtabulation);
                }
            }
        }
        if (sendOrdersVo.getRealname() != null) {
            for (String s : sendOrdersVo.getRealname()) {
                // 派单-车辆-员工关系表添加 “员工” 数据
                subtabulation.setDistributionType(1);
                subtabulation.setTypeId(s);
                sendOrdersSubtabulationMapper.insert(subtabulation);
            }
        }
    }

    @Override
    public List<TaskVo> selectTheSameMonthSendOrders(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        try {
            date1 = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sendOrdersMapper.selectTheSameMonthSendOrders(date1);
    }

    @Override
    public IPage<SendOrdersTaskVo> taskList(String date, Page<SendOrdersTaskVo> page) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        try {
            if (StrUtil.isNotBlank(date)) {
                // 日期不为空则进行日期转换
                date1 = sdf.parse(date);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<SendOrdersTaskVo> list = sendOrdersMapper.taskList(date1, page);
        for (SendOrdersTaskVo sendOrdersTaskVo : list) {
            SendOrdersTaskVo sendOrdersTaskVo1 = sendOrdersMapper.getPlan(sendOrdersTaskVo.getPlanTypeName(), sendOrdersTaskVo.getPlanId());
            sendOrdersTaskVo.setPlanType(sendOrdersTaskVo1.getPlanType());
            sendOrdersTaskVo.setProjectName(sendOrdersTaskVo1.getProjectName());
        }
        return page.setRecords(list);
    }

    @Override
    public List<String> yearsList() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String time = sdf.format(date);
        int tim = Integer.parseInt(time);
        for (int i = 0; i < 5; i++) {
            int ye = tim - i;
            list.add(Integer.toString(ye));
        }
        return list;
    }

    @Override
    public Integer saveSendOrdersVo(SendOrdersVo sendOrdersVo, Date date, String name) {
        baseMapper.saveSendOrders(sendOrdersVo, date, name);
        return sendOrdersVo.getId();
    }

    @Override
    public List<PlanVo> selectPlan2Accomplish(String projectNo, String id, String planType, String sendOrdersId, Page<PlanVo> page) {
        List<PlanVo> list = deliverStorageMapper.selectPlan2DS(projectNo, id, planType, sendOrdersId, page);
        for (PlanVo planVo : list) {
            if (planVo.getReceiptPhotoss() != null) {
                String[] photos = planVo.getReceiptPhotoss().split(",");
                planVo.setReceiptPhotos(photos[0]);
            }
            if (planVo.getScenePhotos() != null) {
                String[] photos2 = planVo.getScenePhotos().split(",");
                planVo.setScenePhotos1(photos2[0]);
                if (photos2.length > 1) {
                    planVo.setScenePhotos2(photos2[1]);
                }
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Result<?> planedit(PlanVo planVo) {
        // 完单状态[0:未完单/1:已完单]
        planVo.setState(1);
        // 判断是否上传了多张异常图片
        if (planVo.getScenePhotos1() != null) {
            // 添加异常图片1
            planVo.setScenePhotos(planVo.getScenePhotos1());
            if (planVo.getScenePhotos2() != null) {
                // 添加异常图片2
                planVo.setScenePhotos(planVo.getScenePhotos1() + "," + planVo.getScenePhotos2());
            }
        }
        // 入库操作
        if (planVo.getOperatorSchema() == 1) {
            // 更新入库完单表信息
            deliverStorageMapper.updateDS(planVo);
            // 根据计划 id 查询入库/完单信息
            DeliverStorage deliverStorage = deliverStorageMapper.selectById(planVo.getId());
            // 根据目标仓库库位 id 查询对应的库位信息
            StorageLocation storageLocation = storageLocationMapper.selectById(planVo.getStorageLocationId());
            // 根据入库完单表的派单 id 查询派单信息
            SendOrders sendOrders = sendOrdersMapper.selectById(deliverStorage.getSendOrdersId());
            // 派单出库时选择库位
            sendOrders.setStorageLocationId(planVo.getStorageLocationId());
            // 派单出库时选择仓库
            sendOrders.setWarehouseId(planVo.getWarehouseId());
            // 修改派单信息
            sendOrdersMapper.updateById(sendOrders);
            // 创建[库存表]条件构造器
            QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
            // 构造派单信息的项目编号作为条件
            queryWrapper.eq("project_no", sendOrders.getProjectNo());
            // 构造派单信息的库位 id 作为条件
            queryWrapper.eq("storage_location_id", sendOrders.getStorageLocationId());
            // 构造入库/完单信息的物料 id 作为条件
            queryWrapper.eq("material_id", deliverStorage.getMaterialId());
            //电缆回收规格 作为条件
            queryWrapper.eq("recycling_specifications", deliverStorage.getRecyclingSpecifications());
            //资产编号 作为条件
            queryWrapper.eq("asset_no", planVo.getAssetNo());
            // 构造入库/完单信息的完单数量单位作为条件
            queryWrapper.eq("backup3", deliverStorage.getAccomplishNumUnit());
            // 查询到库存信息 Inventory
            Inventory inventory1 = inventoryService.getOne(queryWrapper);
            // 判断库存信息是否存在
            if (inventory1 != null) {
                // 库存信息存在
                // 设置库存数量 [库存数量 + 入库完单数量]
                inventory1.setInventoryQuantity(inventory1.getInventoryQuantity().add(deliverStorage.getAccomplishNum()));
                // 设置库存的库存重量，出库完单重量(电缆)   --liu
                // 只有 plan4 电缆2 有库存重量
                if (planVo.getAccomplishWeight() != null)
                    // 转型做加法，存储加法后的重量
                    // 库存重量 = [ 库存重量 + 出库重量 ]
                    inventory1.setBackup5(inventory1.getBackup5().add(planVo.getAccomplishWeight()));
                // 更新库存数据
                inventoryService.updateById(inventory1);
            } else {
                // 库存信息不存在
                Inventory inventory = new Inventory();
                // 设置库存的仓库 id 为派单表的仓库 id
                inventory.setWarehouseId(sendOrders.getWarehouseId());
                // 设置库存的物料 id 为入库完单的物料 id
                inventory.setMaterialId(deliverStorage.getMaterialId());
                // 设置库存的项目编号为派单的项目编号
                inventory.setProjectNo(sendOrders.getProjectNo());
                // 设置库存的库位 id 为派单的库位 id
                inventory.setStorageLocationId(sendOrders.getStorageLocationId());
                // 设置库存的库存数量为入库的完单数量
                inventory.setInventoryQuantity(deliverStorage.getAccomplishNum());
                // 设置库存的计划 id 为派单的计划 Id
                inventory.setBackup1(sendOrders.getPlanId());
                // 设置库存的计划表名为派单的计划类型
                inventory.setBackup2(Integer.parseInt(sendOrders.getPlanType()));
                // 设置库存的单位为入库的完单数量单位
                inventory.setBackup3(deliverStorage.getAccomplishNumUnit());
                //BigDecimal.ROUND_HALF_UP : 保留2位小数
                // 待定[目前不知道什么意思?]
                inventory.setBackup4(deliverStorage.getAccomplishVolume().divide(deliverStorage.getAccomplishNum(), BigDecimal.ROUND_HALF_UP));
                // 设置库存的库存重量，为入库完单重量(电缆)
                if (null != planVo.getAccomplishWeight())
                    inventory.setBackup5(planVo.getAccomplishWeight());
                if (null != planVo.getRecyclingSpecifications())
                    //电缆规格  例：10KV 3*400 铜
                    inventory.setRecyclingSpecifications(planVo.getRecyclingSpecifications() + " " + planVo.getTexture());
                //资产编号存入库存表
                inventory.setAssetNo(planVo.getAssetNo());
                // 新增库存信息
                inventoryService.save(inventory);
            }
            // 判断完单容积是否存在
            if (planVo.getAccomplishVolume() != null) {
                // 完单容积存在则更新库位的当前容积为 [库位当前容积 + 计划的完单容积]
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().add(planVo.getAccomplishVolume()));
                // 更新库位表信息
                storageLocationMapper.updateById(storageLocation);
                return Result.ok("入库完单成功!");
            }
            //回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("入库完单失败!");
        } else {
            // 出库操作
            // 更新出库完单表信息
            receivingStorageMapper.updateDS(planVo);
            // 根据要出库的派单 id 查询出库/完单表信息
            ReceivingStorage receivingStorage = receivingStorageMapper.selectById(planVo.getId());
            // 根据出库/完单表的派单 id 查询派单信息
            SendOrders sendOrders = sendOrdersMapper.selectById(receivingStorage.getSendOrdersId());
            // 设置派单信息的库位 id 为计划出库的目标仓库库位 id
            sendOrders.setStorageLocationId(planVo.getStorageLocationId());
            // 设置派单信息的仓库 id 为计划出库的目标仓库 id
            sendOrders.setWarehouseId(planVo.getWarehouseId());
            // 修改派单信息
            sendOrdersMapper.updateById(sendOrders);
            // 根据出库/完单的库位 id 查询库位信息
            StorageLocation storageLocation = storageLocationMapper.selectById(receivingStorage.getStorageLocationId());
            // 创建[库存表]条件构造器
            QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
            // 构建派单计划id为条件
            queryWrapper.eq("backup1", sendOrders.getPlanId());
            // 构建派单计划类型为条件
            queryWrapper.eq("backup2", sendOrders.getPlanType());
            // 构建派单库位 id 为条件
            queryWrapper.eq("storage_location_id", planVo.getStorageLocationId());
            // 构建出库/完单物料 id 为条件
            queryWrapper.eq("material_id", receivingStorage.getMaterialId());
            // 构建出库的完单数量单位为条件
            queryWrapper.eq("backup3", receivingStorage.getAccomplishNumUnit());
            BigDecimal bigDecimal = new BigDecimal(0);
            // 根据条件构造器查询库存信息
            Inventory inventory = inventoryService.getOne(queryWrapper);
            // 判断库存信息是否存在
            if (inventory == null) {
                //回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("出库完单失败！库存信息不存在!");
            }
            // 库存信息存在
            // 判断[出库/完单表]出库数量是否 > [库存表]库存数量
            if (receivingStorage.getAccomplishNum().doubleValue() > inventory.getInventoryQuantity().doubleValue()) {
                // 进入判断代表[库存表]库存数量不足以完成出库操作
                // 回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("数量不足!库存数为" + inventory.getInventoryQuantity());
            }

            // 更新库存的库存重量，出库完单重量(电缆)   --liu
            // 只有 plan4 电缆2 有库存重量
            if (planVo.getAccomplishWeight() != null) {
                // 当出库完单重量小于等于库存重量，可以进行出库
                if (planVo.getAccomplishWeight().doubleValue() <= Double.parseDouble(inventory.getBackup5().toString())) {
                    // 转型做加法，存储加法后的重量
                    // 库存重量 = [ 库存重量 - 出库重量 ]
                    inventory.setBackup5(inventory.getBackup5().subtract(planVo.getAccomplishWeight()));
                } else {
                    // 库存重量不足，不可以完成出库完单操作
                    // 回滚事务
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.error("可出库重量不足！还剩：" + planVo.getAccomplishWeight().toString());
                }
            }
            // 库存数量充足,可以进行出库操作
            // 设置[库存表]库存数量为 [库存数量 - 出库数量]
            inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(receivingStorage.getAccomplishNum()));
            bigDecimal = bigDecimal.add(inventory.getBackup4());
            // 经过出库操作,判断库存中是否还余留数量
            if (inventory.getInventoryQuantity().doubleValue() <= 0) {
                // 库存中无余留数量直接删除库存信息
                inventoryService.removeById(inventory);
            }
            // 库存中还有余留,则进行修改库存信息
            inventoryService.updateById(inventory);
            // 库位的当前容积 - 出库完单数量
            BigDecimal num = storageLocation.getTheCurrentVolume().subtract(planVo.getAccomplishNum().multiply(bigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP));
            if (num.intValue() < 0) {
                BigDecimal bd2 = new BigDecimal(0);
                // 设置库位的当前容积为 0
                storageLocation.setTheCurrentVolume(bd2);
                // 修改库位信息
                storageLocationMapper.updateById(storageLocation);
                return Result.ok("出库完单成功!");
            }
            // 设置库位的当前容积为 (库位当前容积 - 出库完单数量)
            storageLocation.setTheCurrentVolume(num);
            // 修改库位信息
            storageLocationMapper.updateById(storageLocation);
            return Result.ok("出库完单成功!");
        }
    }

    @Override
    public IPage<SendOrdersVo> selectSendOrdersController(String ids, String planType, Page<SendOrdersVo> page) {
        return page.setRecords(baseMapper.selectSendOrdersController(Arrays.asList(ids.split(",")), planType, page));
    }

    @Override
    public void updatePlanState(Integer planId, String planType) {
        baseMapper.updatePlanState(planId, planType);
    }

    @Transactional
    @Override
    public void removeSendOrders(String id) {
        // 根据派单 id 获取派单信息
        SendOrders sendOrders = sendOrdersMapper.selectById(id);
        // 根据派单 id 删除派单记录
        sendOrdersMapper.deleteById(id);
        // 根据派单信息中 “计划id” 和 “计划类型” 查询计划是否还有派单记录
        Integer selectCount = sendOrdersMapper.selectCount(new QueryWrapper<SendOrders>().eq("plan_id", sendOrders.getPlanId()).eq("plan_type", sendOrders.getPlanType()));
        // 如果没有派单记录，就更新计划表，setSendOrdersState(0); 字段（0未派单/1已派单）
        if (selectCount == 0) {
            switch (sendOrders.getPlanType()) {
                case "1":
                    Plan1 plan1 = new Plan1();
                    plan1.setId(sendOrders.getPlanId());
                    plan1.setSendOrdersState(0);//状态改为未派单
                    plan1Service.updateById(plan1);
                    break;
                case "2":
                    Plan2 plan2 = new Plan2();
                    plan2.setId(sendOrders.getPlanId());
                    plan2.setSendOrdersState(0);
                    plan2Service.updateById(plan2);
                    break;
                case "3":
                    Plan3 plan3 = new Plan3();
                    plan3.setId(sendOrders.getPlanId());
                    plan3.setSendOrdersState(0);
                    plan3Service.updateById(plan3);
                    break;
                case "4":
                    Plan4 plan4 = new Plan4();
                    plan4.setId(sendOrders.getPlanId());
                    plan4.setSendOrdersState(0);
                    plan4Service.updateById(plan4);
                    break;
                default:
                    throw new JeecgBootException(this.getClass().getName() + "没有对应处理项~");
            }
        }
        //删除 派单-车辆-员工关系表 信息
        sendOrdersSubtabulationMapper.delete(new QueryWrapper<SendOrdersSubtabulation>().eq("send_orders_id", id));
    }

    @Override
    public IPage<SendOrdersVo> selectPlanSendOrdersTheSameDay(Page<SendOrdersVo> page) {
        return page.setRecords(sendOrdersMapper.selectPlanSendOrdersTheSameDay(page));
    }

    @Override
    public IPage<SendOrdersVo> selectSendOrdersWD(String ids, String planType, Page<SendOrdersVo> page) {
        List<String> planId = Arrays.asList(ids.split(","));
        List<SendOrdersVo> list = sendOrdersMapper.selectSendOrdersWD(planId, planType, page);
        for (SendOrdersVo sendOrdersVo : list) {
            //回单照片显示第一张
            if (sendOrdersVo.getReceiptPhotos() != null) {
                String[] photos = sendOrdersVo.getReceiptPhotos().split(",");
                sendOrdersVo.setReceiptPhotos(photos[0]);
            }
            //异常照片两张拆分展示
            if (sendOrdersVo.getPhontos() != null) {
                String[] photos2 = sendOrdersVo.getPhontos().split(",");
                sendOrdersVo.setScenePhotos1(photos2[0]);//异常照片1
                if (photos2.length > 1) {
                    sendOrdersVo.setScenePhotos2(photos2[1]);//异常照片2
                }
            }
        }
        return page.setRecords(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deletelStoragesById(Integer id, String type, SendOrdersVo sendOrdersVo, String tableId) {
        switch (type) {
            case "入库":
                this.updateDeliverStorageInfo(id, sendOrdersVo, tableId);
                return Result.ok("删除入库完单记录成功");
            case "出库":
                this.updateReceivingStorageInfo(id, sendOrdersVo, tableId);
                return Result.ok("删除出库完单记录成功");
            default:
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void updateDeliverStorageInfo(Integer id, SendOrdersVo sendOrdersVo, String tableId) {
        Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", sendOrdersVo.getRawMaterialText()));
        switch (tableId) {
            case "1":
                // 1.删除入库完单记录信息
                deliverStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan1 plan1 = plan1Service.getOne(new QueryWrapper<Plan1>().eq("id", sendOrdersVo.getPlanId()));
                plan1.setAlreadyDeliverStorage(plan1.getAlreadyDeliverStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan1Service.updateById(plan1);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper1.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper1.eq("project_no", plan1.getProjectNo());
                wrapper1.eq("material_id", material.getId());
                wrapper1.eq("asset_no", plan1.getAssetNo());
                Inventory inventory = inventoryService.getOne(wrapper1);
                if (inventory != null) {
                    // 修改库存数量
                    inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory.setBackup4(inventory.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    inventoryService.updateById(inventory);
                    if (inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation != null) {
                    // 修改库位中的容积
                    storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation);
                }
                break;
            case "2":
                // 1.删除入库完单记录信息
                deliverStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan2 plan2 = plan2Service.getOne(new QueryWrapper<Plan2>().eq("id", sendOrdersVo.getPlanId()));
                plan2.setAlreadyDeliverStorage(plan2.getAlreadyDeliverStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan2Service.updateById(plan2);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper2.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper2.eq("project_no", plan2.getProjectNo());
                wrapper2.eq("material_id", material.getId());
                wrapper2.eq("asset_no", plan2.getRetiredAssetNo());
                Inventory inventory2 = inventoryService.getOne(wrapper2);
                if (inventory2 != null) {
                    // 修改库存数量
                    inventory2.setInventoryQuantity(inventory2.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory2.setBackup4(inventory2.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    inventoryService.updateById(inventory2);
                    if (inventory2.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory2.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation2 = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation2 != null) {
                    // 修改库位中的容积
                    storageLocation2.setTheCurrentVolume(storageLocation2.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation2);
                }
                break;
            case "3":
                // 1.删除入库完单记录信息
                deliverStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan3 plan3 = plan3Service.getOne(new QueryWrapper<Plan3>().eq("id", sendOrdersVo.getPlanId()));
                plan3.setAlreadyDeliverStorage(plan3.getAlreadyDeliverStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan3Service.updateById(plan3);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper3 = new QueryWrapper<>();
                wrapper3.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper3.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper3.eq("project_no", plan3.getProjectNo());
                wrapper3.eq("material_id", material.getId());
                wrapper3.eq("asset_no", plan3.getProTheorderNo());
                Inventory inventory3 = inventoryService.getOne(wrapper3);
                if (inventory3 != null) {
                    // 修改库存数量
                    inventory3.setInventoryQuantity(inventory3.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory3.setBackup4(inventory3.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    inventoryService.updateById(inventory3);
                    if (inventory3.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory3.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation3 = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation3 != null) {
                    // 修改库位中的容积
                    storageLocation3.setTheCurrentVolume(storageLocation3.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation3);
                }
                break;
            case "4":
                // 1.删除入库完单记录信息
                DeliverStorage deliverStorage = deliverStorageService.getById(id);
                deliverStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan4 plan4 = plan4Service.getOne(new QueryWrapper<Plan4>().eq("id", sendOrdersVo.getPlanId()));
                plan4.setAlreadyDeliverStorage(plan4.getAlreadyDeliverStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan4Service.updateById(plan4);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper4 = new QueryWrapper<>();
                wrapper4.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper4.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper4.eq("project_no", plan4.getProjectNo());
                String plan4MaterialName = deliverStorage.getRecyclingSpecifications().concat(" ").concat(deliverStorage.getTexture());
                Material material4 = materialService.getOne(new QueryWrapper<Material>().eq("name", plan4MaterialName));
                wrapper4.eq("material_id", material4.getId());
                wrapper4.eq("recycling_specifications", plan4MaterialName);
                Inventory inventory4 = inventoryService.getOne(wrapper4);
                if (inventory4 != null) {
                    // 修改库存数量
                    inventory4.setInventoryQuantity(inventory4.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory4.setBackup4(inventory4.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    // 修改库存电缆重量
                    inventory4.setBackup5(inventory4.getBackup5().subtract(sendOrdersVo.getAccomplishWeight()));
                    inventoryService.updateById(inventory4);
                    if (inventory4.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory4.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation4 = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation4 != null) {
                    // 修改库位中的容积
                    storageLocation4.setTheCurrentVolume(storageLocation4.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation4);
                }
                break;
            default:
                throw new JeecgBootException(this.getClass().getName() + "没有对应处理项~");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void updateReceivingStorageInfo(Integer id, SendOrdersVo sendOrdersVo, String tableId) {
        Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", sendOrdersVo.getRawMaterialText()));
        switch (tableId) {
            case "1":
                // 1.删除出库完单记录信息
                receivingStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan1 plan1 = plan1Service.getOne(new QueryWrapper<Plan1>().eq("id", sendOrdersVo.getPlanId()));
                plan1.setAlreadyReceivingStorage(plan1.getAlreadyReceivingStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan1Service.updateById(plan1);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper1.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper1.eq("project_no", plan1.getProjectNo());
                wrapper1.eq("material_id", material.getId());
                wrapper1.eq("asset_no", plan1.getAssetNo());
                Inventory inventory = inventoryService.getOne(wrapper1);
                if (inventory != null) {
                    // 修改库存数量
                    inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory.setBackup4(inventory.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    inventoryService.updateById(inventory);
                    if (inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation != null) {
                    // 修改库位中的容积
                    storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation);
                }
                break;
            case "2":
                // 1.删除出库完单记录信息
                receivingStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan2 plan2 = plan2Service.getOne(new QueryWrapper<Plan2>().eq("id", sendOrdersVo.getPlanId()));
                plan2.setAlreadyReceivingStorage(plan2.getAlreadyReceivingStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan2Service.updateById(plan2);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper2.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper2.eq("project_no", plan2.getProjectNo());
                wrapper2.eq("material_id", material.getId());
                wrapper2.eq("asset_no", plan2.getRetiredAssetNo());
                Inventory inventory2 = inventoryService.getOne(wrapper2);
                if (inventory2 != null) {
                    // 修改库存数量
                    inventory2.setInventoryQuantity(inventory2.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory2.setBackup4(inventory2.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    inventoryService.updateById(inventory2);
                    if (inventory2.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory2.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation2 = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation2 != null) {
                    // 修改库位中的容积
                    storageLocation2.setTheCurrentVolume(storageLocation2.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation2);
                }
                break;
            case "3":
                // 1.删除出库完单记录信息
                receivingStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan3 plan3 = plan3Service.getOne(new QueryWrapper<Plan3>().eq("id", sendOrdersVo.getPlanId()));
                plan3.setAlreadyReceivingStorage(plan3.getAlreadyReceivingStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan3Service.updateById(plan3);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper3 = new QueryWrapper<>();
                wrapper3.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper3.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper3.eq("project_no", plan3.getProjectNo());
                wrapper3.eq("material_id", material.getId());
                wrapper3.eq("asset_no", plan3.getProTheorderNo());
                Inventory inventory3 = inventoryService.getOne(wrapper3);
                if (inventory3 != null) {
                    // 修改库存数量
                    inventory3.setInventoryQuantity(inventory3.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory3.setBackup4(inventory3.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    inventoryService.updateById(inventory3);
                    if (inventory3.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory3.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation3 = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation3 != null) {
                    // 修改库位中的容积
                    storageLocation3.setTheCurrentVolume(storageLocation3.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation3);
                }
                break;
            case "4":
                // 1.删除出库完单记录信息
                ReceivingStorage receivingStorage = receivingStorageService.getById(id);
                receivingStorageService.removeById(id);
                // 2.将计划中累计的已入库数回退
                Plan4 plan4 = plan4Service.getOne(new QueryWrapper<Plan4>().eq("id", sendOrdersVo.getPlanId()));
                plan4.setAlreadyReceivingStorage(plan4.getAlreadyReceivingStorage().subtract(sendOrdersVo.getAccomplishNum()));
                plan4Service.updateById(plan4);
                // 3.修改库存中完单的数量
                QueryWrapper<Inventory> wrapper4 = new QueryWrapper<>();
                wrapper4.eq("warehouse_id", sendOrdersVo.getWarehouseId());
                wrapper4.eq("storage_location_id", sendOrdersVo.getStorageLocationId());
                wrapper4.eq("project_no", plan4.getProjectNo());
                String plan4MaterialName = receivingStorage.getRecyclingSpecifications().concat(" ").concat(receivingStorage.getTexture());
                Material material4 = materialService.getOne(new QueryWrapper<Material>().eq("name", plan4MaterialName));
                wrapper4.eq("material_id", material4.getId());
                wrapper4.eq("recycling_specifications", plan4MaterialName);
                Inventory inventory4 = inventoryService.getOne(wrapper4);
                if (inventory4 != null) {
                    // 修改库存数量
                    inventory4.setInventoryQuantity(inventory4.getInventoryQuantity().subtract(sendOrdersVo.getAccomplishNum()));
                    // 修改容积
                    inventory4.setBackup4(inventory4.getBackup4().subtract(sendOrdersVo.getAccomplishVolume()));
                    // 修改库存电缆重量
                    inventory4.setBackup5(inventory4.getBackup5().subtract(sendOrdersVo.getAccomplishWeight()));
                    inventoryService.updateById(inventory4);
                    if (inventory4.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                        // 库存数为0时删除此库存记录
                        inventoryService.removeById(inventory4.getId());
                    }
                }
                // 4.将库位中累积的容积回退
                StorageLocation storageLocation4 = storageLocationService.getById(sendOrdersVo.getStorageLocationId());
                if (storageLocation4 != null) {
                    // 修改库位中的容积
                    storageLocation4.setTheCurrentVolume(storageLocation4.getTheCurrentVolume().subtract(sendOrdersVo.getAccomplishVolume()));
                    storageLocationService.updateById(storageLocation4);
                }
                break;
            default:
                throw new JeecgBootException(this.getClass().getName() + "没有对应处理项~");
        }
    }

}
