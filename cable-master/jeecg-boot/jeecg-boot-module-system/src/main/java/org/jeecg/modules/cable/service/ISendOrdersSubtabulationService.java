package org.jeecg.modules.cable.service;

import org.jeecg.modules.cable.entity.SendOrdersSubtabulation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * 派单-车辆-员工关系表
 */
public interface ISendOrdersSubtabulationService extends IService<SendOrdersSubtabulation> {

    /**
     * 新增派单-员工/车辆
     */
    void saveSendOrdersSubtabulation(Integer sendOrdersId, Integer distributionType, String typeId, Date taskTime);
}
