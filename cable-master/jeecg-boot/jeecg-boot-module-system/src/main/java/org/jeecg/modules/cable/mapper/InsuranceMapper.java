package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Insurance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.vo.InsuranceListVo;

import java.util.List;

/**
 * 车保险表
 */
public interface InsuranceMapper extends BaseMapper<Insurance> {
    /**
     * 根据车牌号码查询车辆保险信息
     */
    List<InsuranceListVo> getInsurancePage(@Param("license") String license, @Param("page") Page<InsuranceListVo> page);
}
