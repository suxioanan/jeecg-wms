package org.jeecg.modules.cable.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ruan
 * ClassName: InventoryVo <br/>
 * Description: <br/>
 * date: 2020/6/10 10:52<br/>
 *
 * @author Administrator<br />
 * @since JDK 1.8
 */
@Data
public class InventoryVo implements Serializable {
    /**
     * 库存id
     * liu
     * 2020/7/17
     */
    private String id;
    /**
     * 计划类型
     */
    private String pType;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 库位名称
     */
    private String storageLocationName;
    /**
     * 库位id
     */
    private Integer storageLocationId;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 物料编号
     */
    private String serial;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料简称
     */
    private String abbreviation;
    /**
     * 库存数量
     */
    private BigDecimal inventoryQuantity;
    /**
     * 单位
     */
    @Dict(dicCode = "unit")
    private String dw;
    /**
     * 交接单号
     */
    private String pItemSlip;
    /**
     * 资产编号
     */
    private String pAssetNo;
    /**
     * 规格型号
     */
    private String ations;
    /**
     * 电缆重量
     */
    private String availableWeight;
    /**
     * 采购订单号
     */
    private String proTheorderNo;
    /**
     * 电缆回收规格
     */
    private String recyclingSpecifications;
    /**
     * 资产编号
     */
    private String assetNo;
}
