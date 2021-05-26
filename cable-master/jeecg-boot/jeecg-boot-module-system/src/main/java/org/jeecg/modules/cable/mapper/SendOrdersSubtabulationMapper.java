package org.jeecg.modules.cable.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.SendOrdersSubtabulation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;

/**
 * 派单-车辆-员工关系表
 */
public interface SendOrdersSubtabulationMapper extends BaseMapper<SendOrdersSubtabulation> {
    /**
     * 新增派单-员工/车辆
     */
    void saveSendOrdersSubtabulation(@Param("sendOrdersId") Integer sendOrdersId, @Param("distributionType") Integer distributionType, @Param("typeId") String typeId, @Param("taskTime") Date taskTime);
}
