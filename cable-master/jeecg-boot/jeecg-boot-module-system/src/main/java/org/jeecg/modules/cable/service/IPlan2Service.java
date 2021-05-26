package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.Plan2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.dto.Plan2Im;
import org.jeecg.modules.cable.vo.IndexBPTJVo;
import org.jeecg.modules.cable.vo.Plan2Vo;
import org.jeecg.modules.cable.vo.SendOrdersVo;

import java.io.Serializable;
import java.util.List;

/**
 * 计划表2
 */
public interface IPlan2Service extends IService<Plan2> {
    /**
     * 计划2合并完单
     */
    Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList);

    /**
     * 查询计划2批量出库完单的数据
     */
    List<Plan2Vo> getPlan2ReceivingStorageList(List<Serializable> ids);

    /**
     * 查询计划2批量入库完单的数据
     */
    List<Plan2> getPlan2DeliverStorage(List<Serializable> ids);

    /**
     * 根据ids集合条件查询
     */
    List<Plan2> idsqueryRuList(List<String> ids);

    /**
     * 根据ids集合条件查询
     */
    List<SendOrdersVo> idsqueryChuList(List<String> ids);

    /**
     * 分页展示计划表2数据
     */
    IPage<Plan2> pageList(Plan2 plan2, Page<Plan2> page);

    /**
     * 导出 plan2
     */
    List<Plan2Im> exportPlan2(Plan2 plan2, String explain);

    /**
     * 首页备品统计物料出入库数量
     */
    List<IndexBPTJVo> getBPTJList(IndexBPTJVo vo);
}
