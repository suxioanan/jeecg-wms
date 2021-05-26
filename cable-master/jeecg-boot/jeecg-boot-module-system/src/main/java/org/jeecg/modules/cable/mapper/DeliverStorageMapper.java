package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.DeliverStorage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.vo.PlanVo;

import java.util.List;

/**
 * 入库/完单表
 */
public interface DeliverStorageMapper extends BaseMapper<DeliverStorage> {
    /**
     * 根据项目编号查询入库完单信息
     */
    List<PlanVo> selectPlan2DS(@Param("projectNo") String projectNo, @Param("id") String id,
                               @Param("planType") String planType, @Param("sendOrdersId") String sendOrdersId,
                               @Param("page") Page<PlanVo> page);

    /**
     * 根据id修改入库信息
     */
    void updateDS(@Param("PlanVo") PlanVo planVo);
}
