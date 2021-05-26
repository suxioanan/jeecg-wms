package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.modules.cable.entity.StorageLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.StorageLocationVo;

import java.util.List;

/**
 * 库位表
 */
public interface IStorageLocationService extends IService<StorageLocation> {
    /**
     * 查询库位信息
     */
    List<StorageLocationVo> getAll(Wrapper<?> wrapper);
}
