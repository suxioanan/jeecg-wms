package org.jeecg.modules.cable.service.impl;

import org.jeecg.modules.cable.entity.ReceivingStorage;
import org.jeecg.modules.cable.mapper.ReceivingStorageMapper;
import org.jeecg.modules.cable.service.IReceivingStorageService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 出库/完单表
 */
@Service
public class ReceivingStorageServiceImpl extends ServiceImpl<ReceivingStorageMapper, ReceivingStorage> implements IReceivingStorageService {

}
