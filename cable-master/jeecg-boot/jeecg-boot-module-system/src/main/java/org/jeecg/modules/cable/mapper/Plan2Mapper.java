package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.dto.Plan2Im;
import org.jeecg.modules.cable.vo.IndexBPTJVo;
import org.jeecg.modules.cable.vo.Plan2Vo;
import org.jeecg.modules.cable.vo.SendOrdersVo;

import java.io.Serializable;
import java.util.List;

/**
 * 计划表2
 */
public interface Plan2Mapper extends BaseMapper<Plan2> {
    /**
     * 计划1批量派单的数据
     */
    List<SendOrdersVo> idsqueryChuList(@Param("ids") List<String> ids);

    /**
     * 查询计划2批量出库完单的数据
     */
    List<Plan2Vo> getPlan2ReceivingStorageList(@Param("ids") List<Serializable> ids);

    /**
     * 查询计划2批量入库完单的数据
     */
    List<Plan2> getPlan2DeliverStorage(@Param("ids") List<Serializable> ids);

    /**
     * 分页展示计划表2数据
     */
    List<Plan2> pageList(@Param("plan2") Plan2 plan2, @Param("page") Page<Plan2> page);

    /**
     * 导出计划表2数据
     */
    List<Plan2Im> exportPlan2(@Param("plan2") Plan2 plan2);

    /**
     * 首页备品统计物料出入库数量
     */
    List<IndexBPTJVo> getBPTJList(@Param("vo") IndexBPTJVo vo);
}
