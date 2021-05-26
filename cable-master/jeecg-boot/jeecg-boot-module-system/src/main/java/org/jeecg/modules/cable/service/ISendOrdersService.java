package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.SendOrders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.*;

import java.util.Date;
import java.util.List;

/**
 * 派单表
 */
public interface ISendOrdersService extends IService<SendOrders> {

    /**
     * 派单编辑操作
     */
    void MergePlanEdit(SendOrdersVo sendOrdersVo, List<SendOrdersVo> sendOrdersList, List<SendOrdersTaskVo> vehicleList);

    /**
     * 合并派单操作
     */
    void saveMain(SendOrdersVo sendOrdersVo, List<SendOrdersVo> sendOrdersList, List<SendOrdersTaskVo> vehicleList);

    /**
     * 根据年份和月份查询当月每日的车辆任务的数量
     */
    List<TaskVo> selectTheSameMonthSendOrders(String date);

    /**
     * 根据日期查询当天车辆任务信息
     */
    IPage<SendOrdersTaskVo> taskList(String date, Page<SendOrdersTaskVo> page);

    /**
     * 返回近5年年份
     */
    List<String> yearsList();

    /**
     * 派单
     */
    Integer saveSendOrdersVo(SendOrdersVo sendOrdersVo, Date date, String name);

    /**
     * 根据项目编号查询入库出库完单信息
     */
    List<PlanVo> selectPlan2Accomplish(String projectNo, String id, String planType, String sendOrdersId, Page<PlanVo> page);

    /**
     * 完单操作
     */
    Result<?> planedit(PlanVo planVo);

    /**
     * 根据计划id和计划类型查询历史派单记录
     */
    IPage<SendOrdersVo> selectSendOrdersController(String ids, String planType, Page<SendOrdersVo> page);

    void updatePlanState(Integer planId, String planType);

    void removeSendOrders(String id);

    IPage<SendOrdersVo> selectPlanSendOrdersTheSameDay(Page<SendOrdersVo> page);

    IPage<SendOrdersVo> selectSendOrdersWD(String ids, String planType, Page<SendOrdersVo> page);

    /**
     * 通过id删除完单记录信息
     */
    Result<?> deletelStoragesById(Integer id, String type, SendOrdersVo sendOrdersVo, String tableId);
}
