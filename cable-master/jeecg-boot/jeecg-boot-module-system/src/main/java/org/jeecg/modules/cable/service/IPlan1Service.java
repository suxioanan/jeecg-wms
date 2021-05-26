package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.Plan1;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.dto.Plan1Im;
import org.jeecg.modules.cable.vo.*;

import java.io.Serializable;
import java.util.List;

/**
 * 计划表1
 */
public interface IPlan1Service extends IService<Plan1> {
    /**
     * 计划1合并完单
     */
    Result<?> consolidationCompleted(List<Serializable> plan1Ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList);

    /**
     * 查询计划1批量出库完单的数据
     */
    List<Plan1Vo> getPlan1ReceivingStorageList(List<Serializable> ids);

    /**
     * 查询计划1批量入库完单的数据
     */
    List<Plan1> getPlan1DeliverStorage(List<Serializable> ids);

    /**
     * 根据ids集合条件查询
     */
    IPage<Plan1> pageList(Plan1Vo plan1Vo, Page<Plan1> page);

    /**
     * 根据ids集合条件查询
     */
    List<Plan1> idsqueryRuList(List<String> ids);

    /**
     * 根据ids集合条件查询
     */
    List<SendOrdersVo> idsqueryChuList(List<String> ids);

    /**
     * 导出 plan1
     */
    List<Plan1Im> exportPlan1(Plan1Im plan1Im, String explain);

    IPage<SettleAccountsVo> selectSettleAccounts(String backup1, String planType, String projectNo, Page<SettleAccountsVo> page);

    IPage<SettleAccountsDetailsVo> selectSettleAccountsDetails(String projectNo, Page<SettleAccountsDetailsVo> page);

    /**
     * 计划表1配变电/线路统计
     */
    IPage<Plan1Vo> selectSubstation(String wasteMaterialText, String beginTime, String endTime, String planType, Page<Plan1Vo> page);

    /**
     * 导出变电/导线统计数据
     */
    List<Plan1ExcelVo> exportPlan2(Plan1ExcelVo plan1ExcelVo);

    /**
     * 首页线路统计
     */
    List<IndexXLTJVo> getXLTJList(IndexXLTJVo vo);
}
