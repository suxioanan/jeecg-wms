package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.Plan3;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.dto.Plan3Im;
import org.jeecg.modules.cable.vo.IndexLCTJVo;
import org.jeecg.modules.cable.vo.Plan3ExcelVo;
import org.jeecg.modules.cable.vo.Plan3Vo;
import org.jeecg.modules.cable.vo.SendOrdersVo;

import java.io.Serializable;
import java.util.List;

/**
 * 计划表3
 */
public interface IPlan3Service extends IService<Plan3> {
    /**
     * 计划3合并完单
     */
    Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList);

    /**
     * 查询计划3批量出库完单的数据
     */
    List<Plan3Vo> getPlan3ReceivingStorageList(List<Serializable> ids);

    /**
     * 查询计划3批量入库完单的数据
     */
    List<Plan3> getPlan3DeliverStorage(List<Serializable> ids);

    /**
     * 根据ids集合条件查询
     */
    List<Plan3> idsqueryRuList(List<String> ids);

    /**
     * 根据ids集合条件查询
     */
    List<SendOrdersVo> idsqueryChuList(List<String> ids);

    /**
     * 分页展示计划表3数据
     */
    IPage<Plan3> pageList(Plan3 plan3, Page<Plan3> page);

    /**
     * 导出计划表3数据
     */
    List<Plan3Im> exportPlan3(Plan3 plan3, String explain);

    /**
     * 计划表3新品统计
     */
    IPage<Plan3Vo> selectNewproducts(String materialDescribe, String beginTime, String endTime, String planType, Page<Plan3Vo> page);

    /**
     * 导出计划表3新品统计数据
     */
    List<Plan3ExcelVo> exportPlan2(Plan3ExcelVo plan3ExcelVo);

    /**
     * 首页临措统计
     */
    List<IndexLCTJVo> getLCTJList(IndexLCTJVo vo);
}
