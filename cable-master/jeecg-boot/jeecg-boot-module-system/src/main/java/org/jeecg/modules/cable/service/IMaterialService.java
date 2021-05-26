package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Material;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 物料表
 */
public interface IMaterialService extends IService<Material> {

    /**
     * 物料出入库台账
     */
    IPage<MaterialOutPutAccountVo> getMaterialOutPutAccountList(MaterialOutPutAccountVo materialOutPutAccountVo, Page<MaterialOutPutAccountVo> page);

    /**
     * 物料余留台账
     */
    IPage<MaterialRemainingAccountVo> getMaterialRemainingAccountList(String serial, String name, String projectNo, Page<MaterialRemainingAccountVo> page);

    /**
     * 物料年度出入台账统计
     */
    IPage<AnnualReportVo> getAnnualAccountList(String planType, String dateTime, String serial, String name, String projectNo, String assetNo, Page<AnnualReportVo> page);

    /**
     * 分页查询物料信息
     */
    IPage<Material> getMaterialPageList(String serial, String name, String supplier, Page<Material> page);

    /**
     * 修改物料信息
     */
    void editMaterial(Material material);

    /**
     * 出入库台账
     */
    IPage<OutPutWarehouseVo> getOutPutWarehouseList(OutPutWarehouseVo vo, Page<OutPutWarehouseVo> page);

    List<Map<String, String>> materialOutPutList(MaterialOutPutAccountVo materialOutPutAccountVo);
}
