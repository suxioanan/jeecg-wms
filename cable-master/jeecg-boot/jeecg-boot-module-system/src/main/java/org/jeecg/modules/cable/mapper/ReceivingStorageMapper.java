package org.jeecg.modules.cable.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.ReceivingStorage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.vo.PlanVo;

/**
 * 出库/完单表
 */
public interface ReceivingStorageMapper extends BaseMapper<ReceivingStorage> {

    /**
     * 根据id修改出库信息
     */
    void updateDS(@Param("PlanVo") PlanVo planVo);
}
