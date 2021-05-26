package org.jeecg.modules.cable.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.vo.MaterialStorageLocationVo;

import java.util.List;

public interface MaterialStorageLocationVoMapper extends BaseMapper<MaterialStorageLocationVo> {
    List<MaterialStorageLocationVo> queryBySid(@Param("sId") Integer storageLocationId);
}
