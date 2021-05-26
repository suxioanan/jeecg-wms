package org.jeecg.modules.cable.service.impl;

import org.jeecg.modules.cable.entity.Vehicle;
import org.jeecg.modules.cable.mapper.VehicleMapper;
import org.jeecg.modules.cable.service.IVehicleService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 车辆表
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements IVehicleService {

}
