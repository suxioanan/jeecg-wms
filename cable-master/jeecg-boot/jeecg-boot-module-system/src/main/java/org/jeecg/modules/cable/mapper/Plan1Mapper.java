package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.dto.Plan1Im;
import org.jeecg.modules.cable.vo.*;

import java.io.Serializable;
import java.util.List;

/**
 * 计划表1
 */
public interface Plan1Mapper extends BaseMapper<Plan1> {
    /**
     * 计划1批量派单的数据
     */
    List<SendOrdersVo> idsqueryChuList(@Param("ids") List<String> ids);

    /**
     * 查询计划1批量出库完单的数据
     */
    List<Plan1Vo> getPlan1ReceivingStorageList(@Param("ids") List<Serializable> ids);

    /**
     * 查询计划1批量入库完单的数据
     */
    List<Plan1> getPlan1DeliverStorage(@Param("ids") List<Serializable> ids);

    /**
     * 条件分页查询计划1
     */
    List<Plan1> pageList(@Param("plan1Vo") Plan1Vo plan1Vo, @Param("page") Page<Plan1> page);

    List<StorageLocationListVo> StorageLocationListVoPage(@Param("storageLocationListVo") StorageLocationListVo storageLocationListVo,
                                                          @Param("page") Page<StorageLocationListVo> page);

    /**
     * 导出计划表1数据
     */
    List<Plan1Im> exportPlan1(@Param("plan1Im") Plan1Im plan1Im);

    List<SettleAccountsVo> selectSettleAccounts(@Param("backup1") String backup1, @Param("planType") String planType,
                                                @Param("projectNo") String projectNo, @Param("page") Page<SettleAccountsVo> page);

    List<SettleAccountsDetailsVo> selectSettleAccountsDetails(@Param("projectNo") String projectNo,
                                                              @Param("page") Page<SettleAccountsDetailsVo> page);

    /**
     * 计划表1配变电/线路统计
     */
    List<Plan1Vo> selectSubstation(@Param("wasteMaterialText") String wasteMaterialText, @Param("beginTime") String beginTime,
                                   @Param("endTime") String endTime, @Param("planType") String planType, @Param("page") Page<Plan1Vo> page);

    /**
     * 导出数据
     */
    List<Plan1ExcelVo> exportPlan2(@Param("plan1ExcelVo") Plan1ExcelVo plan1ExcelVo);

    /**
     * 首页线路统计
     */
    List<IndexXLTJVo> getXLTJList(@Param("vo") IndexXLTJVo vo);
}
