package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.StorageLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.vo.StorageLocationVo;

import java.util.List;

/**
 * 库位表
 */
public interface StorageLocationMapper extends BaseMapper<StorageLocation> {
    /**
     * 查询库位信息
     */
    List<StorageLocationVo> getAll(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
