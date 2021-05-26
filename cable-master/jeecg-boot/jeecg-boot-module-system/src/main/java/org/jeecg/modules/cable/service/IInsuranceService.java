package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Insurance;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.InsuranceListVo;

/**
 * 车保险表
 */
public interface IInsuranceService extends IService<Insurance> {
    /**
     * 根据车牌号码查询车辆保险信息
     */
    IPage<InsuranceListVo> getInsurancePage(InsuranceListVo insurance, Page<InsuranceListVo> page);

}
