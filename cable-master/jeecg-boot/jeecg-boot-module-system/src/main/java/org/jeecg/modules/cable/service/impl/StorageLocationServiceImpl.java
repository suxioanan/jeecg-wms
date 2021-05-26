package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.modules.cable.entity.StorageLocation;
import org.jeecg.modules.cable.mapper.StorageLocationMapper;
import org.jeecg.modules.cable.service.IStorageLocationService;
import org.jeecg.modules.cable.vo.StorageLocationVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 库位表
 */
@Service
public class StorageLocationServiceImpl extends ServiceImpl<StorageLocationMapper, StorageLocation> implements IStorageLocationService {
    @Override
    public List<StorageLocationVo> getAll(Wrapper<?> wrapper) {
        return baseMapper.getAll(wrapper);
    }
}
