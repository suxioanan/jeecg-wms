package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.Plan4;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.dto.Plan4Im;
import org.jeecg.modules.cable.vo.Plan4ExcelVo;
import org.jeecg.modules.cable.vo.Plan4Vo;
import org.jeecg.modules.cable.vo.SendOrdersVo;

import java.io.Serializable;
import java.util.List;

/**
 * 计划表4
 */
public interface IPlan4Service extends IService<Plan4> {
    /**
     * 计划4合并完单
     */
    Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList);

    /**
     * 查询计划4批量出库完单的数据
     */
    List<Plan4Vo> getPlan4ReceivingStorageList(List<Serializable> ids);

    /**
     * 查询计划4批量入库完单的数据
     */
    List<Plan4> getPlan4DeliverStorage(List<Serializable> ids);

    /**
     * 根据ids集合条件查询
     */
    List<Plan4> idsqueryRuList(List<String> ids);

    /**
     * 根据ids集合条件查询
     */
    List<SendOrdersVo> idsqueryChuList(List<String> ids);

    /**
     * 分页展示计划表4数据
     */
    IPage<Plan4> pageList(Plan4 plan4, Page<Plan4> page);

    /**
     * 导出计划表4数据
     */
    List<Plan4Im> exportPlan4(Plan4 plan4, String explain, String beginTime, String endTime);

    /**
     * 导出计划表4汇总数据
     */
    List<Plan4Vo> exportFeedbackSummary(Plan4Vo plan4Vo);

    /**
     * 计划表4电缆统计
     */
    IPage<Plan4Vo> selectCable(String voltageGrade, String beginTime, String endTime, String planType, Page<Plan4Vo> page);

    /**
     * 导出电缆统计数据
     */
    List<Plan4ExcelVo> exportPlan3(Plan4ExcelVo plan4ExcelVo);
}
