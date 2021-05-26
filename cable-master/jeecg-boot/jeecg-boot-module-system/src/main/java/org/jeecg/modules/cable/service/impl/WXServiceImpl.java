package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.mapper.WXMapper;
import org.jeecg.modules.cable.service.IWXService;
import org.jeecg.modules.cable.vo.Deliver_Receiving_StorageVo;
import org.jeecg.modules.cable.vo.PickUpTheTaskVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * WX小程序
 */
@Service
public class WXServiceImpl implements IWXService {
    @Resource
    private WXMapper wxMapper;

    @Override
    public IPage<PickUpTheTaskVo> pickUpTheTask(Integer completeState, Page<PickUpTheTaskVo> page) {
        return page.setRecords(wxMapper.pickUpTheTask(completeState, page));
    }

    @Override
    public Integer deliverStorage(Deliver_Receiving_StorageVo vo) {
        return wxMapper.deliverStorage(vo);
    }

    @Override
    public Integer receivingStorage(Deliver_Receiving_StorageVo vo) {
        return wxMapper.receivingStorage(vo);
    }
}
