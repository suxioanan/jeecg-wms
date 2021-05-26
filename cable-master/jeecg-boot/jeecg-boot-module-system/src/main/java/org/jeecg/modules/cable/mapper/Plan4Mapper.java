package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan4;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.dto.Plan4Im;
import org.jeecg.modules.cable.vo.Plan4ExcelVo;
import org.jeecg.modules.cable.vo.Plan4Vo;
import org.jeecg.modules.cable.vo.SendOrdersVo;

import java.io.Serializable;
import java.util.List;

/**
 * 计划表4
 */
public interface Plan4Mapper extends BaseMapper<Plan4> {
    /**
     * 计划1批量派单的数据
     */
    List<SendOrdersVo> idsqueryChuList(@Param("ids") List<String> ids);

    /**
     * 查询计划4批量出库完单的数据
     */
    List<Plan4Vo> getPlan4ReceivingStorageList(@Param("ids") List<Serializable> ids);

    /**
     * 查询计划4批量入库完单的数据
     */
    List<Plan4> getPlan4DeliverStorage(@Param("ids") List<Serializable> ids);

    /**
     * 分页展示计划表4数据
     */
    List<Plan4> pageList(@Param("plan4") Plan4 plan4, @Param("page") Page<Plan4> page);

    /**
     * 导出计划表4数据
     */
    List<Plan4Im> exportPlan4(@Param("plan4") Plan4 plan4, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 导出计划表4汇总数据
     */
    List<Plan4Vo> exportFeedbackSummary(@Param("plan4Vo") Plan4Vo plan4Vo);

    /**
     * 计划表4电缆统计
     */
    List<Plan4Vo> selectCable(@Param("voltageGrade") String voltageGrade, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("planType") String planType, @Param("page") Page<Plan4Vo> page);

    /**
     * 导出数据
     */
    List<Plan4ExcelVo> exportPlan3(@Param("plan4ExcelVo") Plan4ExcelVo plan4ExcelVo);
}
