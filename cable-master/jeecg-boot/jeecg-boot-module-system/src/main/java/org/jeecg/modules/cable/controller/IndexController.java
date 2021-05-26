package org.jeecg.modules.cable.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.service.IPlan1Service;
import org.jeecg.modules.cable.service.IPlan2Service;
import org.jeecg.modules.cable.service.IPlan3Service;
import org.jeecg.modules.cable.vo.IndexBPTJVo;
import org.jeecg.modules.cable.vo.IndexLCTJVo;
import org.jeecg.modules.cable.vo.IndexXLTJVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页控制器
 *
 * @author: 南独酌酒 <211425401@126.com>
 * @date: 2020/9/16 14:16
 */
@Api(tags = "物流首页接口")
@RestController
@RequestMapping(value = "/index")
public class IndexController {
    @Autowired
    private IPlan1Service plan1Service;
    @Autowired
    private IPlan2Service plan2Service;
    @Autowired
    private IPlan3Service plan3Service;

    /**
     * 首页备品统计
     * bai 2020/9/16
     *
     * @param vo 查询条件
     * @return 备品统计物料出入库数量
     */
    @GetMapping(value = "/getBTPJList")
    public Result<?> getBTPJList(IndexBPTJVo vo) {
        List<IndexBPTJVo> list = plan2Service.getBPTJList(vo);
        return Result.ok(list);
    }

    /**
     * 首页线路统计
     * bai 2020/9/17
     *
     * @param vo 查询条件
     * @return 线路统计数据
     */
    @GetMapping(value = "/getXLTJList")
    public Result<?> getXLTJList(IndexXLTJVo vo) {
        List<IndexXLTJVo> list = plan1Service.getXLTJList(vo);
        return Result.ok(list);
    }

    /**
     * 首页临措统计
     * bai 2020/9/17
     *
     * @param vo 查询条件
     * @return 临措统计数据
     */
    @GetMapping(value = "/getLCTJList")
    public Result<?> getLCTJList(IndexLCTJVo vo) {
        List<IndexLCTJVo> list = plan3Service.getLCTJList(vo);
        return Result.ok(list);
    }
}
