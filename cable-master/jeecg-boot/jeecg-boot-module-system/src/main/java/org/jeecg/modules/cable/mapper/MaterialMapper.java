package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Material;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.vo.*;

import java.util.List;

/**
 * 物料表
 */
public interface MaterialMapper extends BaseMapper<Material> {
    /**
     * 物料出入库台账
     */
    List<MaterialOutPutAccountVo> getMaterialOutPutAccountList(@Param("materialOutPutAccountVo") MaterialOutPutAccountVo materialOutPutAccountVo,
                                                               @Param("page") Page<MaterialOutPutAccountVo> page);

    /**
     * 物料余留台账
     */
    List<MaterialRemainingAccountVo> getMaterialRemainingAccountList(@Param("serial") String serial, @Param("name") String name,
                                                                     @Param("projectNo") String projectNo, @Param("page") Page<MaterialRemainingAccountVo> page);

    /**
     * 物料年度出入台账
     */
    List<AnnualReportVo> getAnnualAccountList(@Param("planType") String planType, @Param("dateTime") String dateTime,
                                              @Param("serial") String serial, @Param("name") String name,
                                              @Param("projectNo") String projectNo, @Param("assetNo") String assetNo,
                                              @Param("page") Page<AnnualReportVo> page);

    /**
     * 分页查询物料信息
     */
    List<Material> getMaterialPageList(@Param("serial") String serial, @Param("name") String name,
                                       @Param("supplier") String supplier, @Param("page") Page<Material> page);

    /**
     * 新增物料
     */
    Integer saveMaterial(Material material);

    /**
     * 修改物料信息
     */
    Integer editMaterial(Material material);

    /**
     * 出入库台账
     */
    List<OutPutWarehouseVo> getOutPutWarehouseList(@Param("vo") OutPutWarehouseVo vo, @Param("page") Page<OutPutWarehouseVo> page);

    StatisticalChartVo materialOutPutDayCList(@Param("date") String date, @Param("id") Integer id);

    StatisticalChartVo materialOutPutDayRList(@Param("date") String date, @Param("id") Integer id);

    StatisticalChartVo materialOutPutMonthRList(@Param("date") String date, @Param("id") Integer id);

    StatisticalChartVo materialOutPutMonthCList(@Param("date") String date, @Param("id") Integer id);
}
