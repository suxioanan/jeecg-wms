package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Material;
import org.jeecg.modules.cable.entity.Warehouse;
import org.jeecg.modules.cable.mapper.MaterialMapper;
import org.jeecg.modules.cable.mapper.WarehouseMapper;
import org.jeecg.modules.cable.service.IWarehouseService;
import org.jeecg.modules.cable.vo.InventoryVo;
import org.jeecg.modules.cable.vo.KuweiVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;

/**
 * 仓库表
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {
    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public IPage<InventoryVo> selectPageinventory(InventoryVo inventoryVo, Page<InventoryVo> page) {
        return page.setRecords(baseMapper.selectPageinventory(inventoryVo, page));
    }

    @Override
    public IPage<InventoryVo> selectInfo(InventoryVo inventoryVo, Page<InventoryVo> page) {
        return page.setRecords(baseMapper.selectInfo(inventoryVo, page));
    }

    @Override
    public List<KuweiVo> kewei(String id) {
        return baseMapper.kewei(id.split(","));
    }

    @Override
    public List<KuweiVo> queryInventory(Serializable projectNo,Serializable materialId) {
        Material serial = materialMapper.selectOne(new QueryWrapper<Material>().eq("serial", materialId));
        return baseMapper.queryInventory(projectNo, serial.getId());
    }
}
