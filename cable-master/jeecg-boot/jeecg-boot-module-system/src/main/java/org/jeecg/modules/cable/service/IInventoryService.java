package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Inventory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.InventoryListsVo;
import org.jeecg.modules.cable.vo.YikuVo;

import java.util.List;

/**
 * 库存表
 */
public interface IInventoryService extends IService<Inventory> {

    /**
     * 根据仓库id和库位id查当前库位存放的物品（分页）
     */
    IPage<InventoryListsVo> InsurancePageList(InventoryListsVo inventoryVo, Page<InventoryListsVo> page);

    List<InventoryListsVo> InsuranceLists(Integer storageLocationId);

    IPage<YikuVo> yikuVoPage(YikuVo yikuVo, Page<YikuVo> page);

    void yikuDel(Integer id);

    void yikuAdd(YikuVo yikuVo);
}
