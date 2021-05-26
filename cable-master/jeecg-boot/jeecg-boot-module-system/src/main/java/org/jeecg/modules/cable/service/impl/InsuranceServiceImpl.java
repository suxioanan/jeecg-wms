package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Insurance;
import org.jeecg.modules.cable.mapper.InsuranceMapper;
import org.jeecg.modules.cable.service.IInsuranceService;
import org.jeecg.modules.cable.vo.InsuranceListVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 车保险表
 */
@Service
public class InsuranceServiceImpl extends ServiceImpl<InsuranceMapper, Insurance> implements IInsuranceService {

    @Override
    public IPage<InsuranceListVo> getInsurancePage(InsuranceListVo insurance, Page<InsuranceListVo> page) {
        List<InsuranceListVo> list = baseMapper.getInsurancePage(insurance.getLicense(), page);
        for (InsuranceListVo insuranceListVo : list) {
            if (insuranceListVo.getInsuranceDateBegin() != null) {
                insuranceListVo.setInsuranceDate(insuranceListVo.getInsuranceDateBegin() + " 至 ?");
                if (insuranceListVo.getInsuranceDateEnd() != null) {
                    insuranceListVo.setInsuranceDate(insuranceListVo.getInsuranceDateBegin() + " 至 " + insuranceListVo.getInsuranceDateEnd());
                }
            }
            if (insuranceListVo.getStrongDateBegin() != null) {
                insuranceListVo.setStrongDate(insuranceListVo.getStrongDateBegin() + " 至 ?");
                if (insuranceListVo.getStrongDateEnd() != null) {
                    insuranceListVo.setStrongDate(insuranceListVo.getStrongDateBegin() + " 至 " + insuranceListVo.getStrongDateEnd());
                }

            }
        }
        return page.setRecords(list);
    }
}
