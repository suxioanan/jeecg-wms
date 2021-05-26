package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.vo.InventoryListsVo;
import org.jeecg.modules.cable.vo.YikuVo;

import java.util.List;

/**
 * 库存表
 */
public interface InventoryMapper extends BaseMapper<Inventory> {
    /**
     * 根据仓库id和库位id查当前库位存放的物品（分页）
     */
    List<InventoryListsVo> InsurancePageList(@Param("inventoryVo") InventoryListsVo inventoryVo, @Param("page") Page<InventoryListsVo> page);

    List<InventoryListsVo> InsurancePageList(@Param("storageLocationId") Integer storageLocationId);

    List<YikuVo> yikuVoPage(@Param("yikuVo") YikuVo yikuVo, @Param("page") Page<YikuVo> page);

    void yikuDel(@Param("id") Integer id);

    void yikuAdd(@Param("val") YikuVo yikuVo);
}
