package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Inventory;
import org.jeecg.modules.cable.mapper.InventoryMapper;
import org.jeecg.modules.cable.service.IInventoryService;
import org.jeecg.modules.cable.vo.InventoryListsVo;
import org.jeecg.modules.cable.vo.YikuVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 库存表
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

    @Override
    public IPage<InventoryListsVo> InsurancePageList(InventoryListsVo inventoryVo, Page<InventoryListsVo> page) {
        return page.setRecords(baseMapper.InsurancePageList(inventoryVo, page));
    }

    @Override
    public List<InventoryListsVo> InsuranceLists(Integer storageLocationId) {
        return baseMapper.InsurancePageList(storageLocationId);
    }

    @Override
    public IPage<YikuVo> yikuVoPage(YikuVo yikuVo, Page<YikuVo> page) {
        return page.setRecords(baseMapper.yikuVoPage(yikuVo, page));
    }

    @Override
    public void yikuDel(Integer id) {
        baseMapper.yikuDel(id);
    }

    @Override
    public void yikuAdd(YikuVo yikuVo) {
        baseMapper.yikuAdd(yikuVo);
    }
}
