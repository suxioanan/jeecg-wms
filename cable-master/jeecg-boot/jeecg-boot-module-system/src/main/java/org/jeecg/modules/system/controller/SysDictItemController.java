package org.jeecg.modules.system.controller;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/sys/dictItem")
@Slf4j
public class SysDictItemController {

    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * @param sysDictItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：查询字典数据
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysDictItem>> queryPageList(SysDictItem sysDictItem, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysDictItem>> result = new Result<IPage<SysDictItem>>();
        QueryWrapper<SysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        queryWrapper.orderByAsc("sort_order");
        Page<SysDictItem> page = new Page<SysDictItem>(pageNo, pageSize);
        IPage<SysDictItem> pageList = sysDictItemService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @return
     * @功能：新增
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<SysDictItem> add(@RequestBody SysDictItem sysDictItem) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        try {
            sysDictItem.setCreateTime(new Date());
            sysDictItemService.save(sysDictItem);
            result.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @param sysDictItem
     * @return
     * @功能：编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<SysDictItem> edit(@RequestBody SysDictItem sysDictItem) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        SysDictItem sysdict = sysDictItemService.getById(sysDictItem.getId());
        if (sysdict == null) {
            result.error500("未找到对应实体");
        } else {
            sysDictItem.setUpdateTime(new Date());
            boolean ok = sysDictItemService.updateById(sysDictItem);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("编辑成功!");
            }
        }
        return result;
    }

    /**
     * @param id
     * @return
     * @功能：删除字典数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<SysDictItem> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        SysDictItem joinSystem = sysDictItemService.getById(id);
        if (joinSystem == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDictItemService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * @param ids
     * @return
     * @功能：批量删除字典数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<SysDictItem> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysDictItemService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 查询所有车辆类型
     *
     * @param
     * @Author Xm
     * @Date 2020/5/19 10:07
     */
    @GetMapping(value = "/selectVehicleType")
    public Result<List<SysDictItem>> selectVehicleType() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("vehicle_type");
        result.setResult(list);
        return result;
    }

    /**
     * 查询备品/临措类型
     *
     * @param
     * @Author Xm
     * @Date 2020/5/25 13:29
     */
    @GetMapping(value = "/selectPlan2Type")
    public Result<List<SysDictItem>> selectPlan2Type() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("plan2_type");
        result.setResult(list);
        return result;
    }

    /**
     * 查询计划1类型
     *
     * @param
     * @Author Xm
     * @Date 2020/5/27 14:38
     */
    @GetMapping(value = "/selectPlan1Type")
    public Result<List<SysDictItem>> selectPlan1Type() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("plan1_type");
        result.setResult(list);
        return result;
    }

    /**
     * 查询计划3类型
     *
     * @param
     * @Author bai
     * @Date 2020/5/29
     */
    @GetMapping(value = "/selectPlan3Type")
    public Result<List<SysDictItem>> selectPlan3Type() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("plan3_type");
        result.setResult(list);
        return result;
    }

    /**
     * 查询计划完成状态
     *
     * @param
     * @Author Xm
     * @Date 2020/5/25 14:04
     */
    @GetMapping(value = "/selectCompleteState")
    public Result<List<SysDictItem>> selectCompleteState() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("completeState");
        result.setResult(list);
        return result;
    }

    /**
     * 查询单位
     *
     * @param
     * @Author Xm
     * @Date 2020/5/26 10:33
     */
    @GetMapping(value = "/getDictItemUnit")
    public Result<List<SysDictItem>> getDictItemUnit() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("unit");
        result.setResult(list);
        return result;
    }

    /**
     * 查询派单类型
     *
     * @param
     * @Author Xm
     * @Date 2020/5/26 10:34
     */
    @GetMapping(value = "/getDictItemSendOrdersType")
    public Result<List<SysDictItem>> getDictItemSendOrdersType() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("send_orders_type");
        result.setResult(list);
        return result;
    }

    /**
     * 查询实际回收规格
     *
     * @Author bai
     * @Date 2020/6/8
     */
    @GetMapping(value = "/getrecyclingSpecifications")
    public Result<List<SysDictItem>> getrecyclingSpecifications() {
        Result<List<SysDictItem>> result = new Result<List<SysDictItem>>();
        List<SysDictItem> list = sysDictItemService.selectType("recyclingSpecifications");
        result.setResult(list);
        return result;
    }


    /**
     * 查询字典数据通用接口
     * bai
     * 2020/7/9
     *
     * @param commonText 通用字典文本
     * @return 字典数据结果集
     */
    @GetMapping(value = "/getSysDictItemCommon")
    public Result<?> getSysDictItemCommon(@RequestParam("commonText") String commonText) {
        List<SysDictItem> sysDictItems = sysDictItemService.selectType(commonText);
        return Result.ok(sysDictItems);
    }
}
