package org.jeecg.modules.cable.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.service.IWXService;
import org.jeecg.modules.cable.vo.Deliver_Receiving_StorageVo;
import org.jeecg.modules.cable.vo.PickUpTheTaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * WX小程序接口
 * bai
 * 2020/6/11
 */
@Api(tags = "WX小程序接口")
@RestController
@RequestMapping("/wx")
@Slf4j
public class WXController {
    @Autowired
    private IWXService wxService;

    /**
     * 通用接口【未完成任务、已完成任务】
     */
    @ApiOperation(value = "接任务", notes = "接任务")
    @GetMapping(value = "/pickUpTheTask")
    public Result<?> pickUpTheTask(@RequestParam(value = "completeState", required = false) Integer completeState,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<PickUpTheTaskVo> page = new Page<>(pageNo, pageSize);
        IPage<PickUpTheTaskVo> pageList = wxService.pickUpTheTask(completeState, page);
        return Result.ok(pageList);
    }

    /**
     * 入库处置
     */
    @ApiOperation(value = "入库处置", notes = "入库处置")
    @PostMapping(value = "/deliverStorage")
    public Result<?> deliverStorage(@RequestBody Deliver_Receiving_StorageVo vo) {
        if (wxService.deliverStorage(vo) == 1) return Result.ok("操作成功！");
        return Result.error("操作有误!");
    }

    /**
     * 出库处置
     */
    @ApiOperation(value = "出库处置", notes = "出库处置")
    @PostMapping(value = "/receivingStorage")
    public Result<?> receivingStorage(@RequestBody Deliver_Receiving_StorageVo vo) {
        if (wxService.receivingStorage(vo) == 1) return Result.ok("操作成功！");
        return Result.error("操作有误!");
    }

    /**
     * 查看个人信息
     */
    @ApiOperation(value = "查看个人信息", notes = "查看个人信息")
    @GetMapping(value = "/myInfo")
    public Result<?> myInfo() {
        return null;
    }

    /**
     * 查看个人详细信息
     */
    @ApiOperation(value = "查看个人详细信息", notes = "查看个人详细信息")
    @GetMapping(value = "/myDetailsInfo")
    public Result<?> myDetailsInfo() {
        return null;
    }
}
