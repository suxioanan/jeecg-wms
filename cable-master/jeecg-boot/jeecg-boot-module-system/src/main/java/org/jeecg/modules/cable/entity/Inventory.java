package org.jeecg.modules.cable.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 库存表
 */
@Data
@TableName("inventory")
@NoArgsConstructor
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    public Inventory(Integer warehouseId, Integer storageLocationId, String projectNo, String projectName, Integer materialId, BigDecimal inventoryQuantity, Date createTime, String createBy, Integer backup1, Integer backup2, Integer backup3, BigDecimal backup4, BigDecimal backup5, String assetNo) {
        this.warehouseId = warehouseId;
        this.storageLocationId = storageLocationId;
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.materialId = materialId;
        this.inventoryQuantity = inventoryQuantity;
        this.createTime = createTime;
        this.createBy = createBy;
        this.backup1 = backup1;
        this.backup2 = backup2;
        this.backup3 = backup3;
        this.backup4 = backup4;
        this.backup5 = backup5;
        this.assetNo = assetNo;
    }

    /**
     * 库存表id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "库存表id")
    private java.lang.Integer id;
    /**
     * 仓库id
     */
    @Excel(name = "仓库id", width = 15)
    @ApiModelProperty(value = "仓库id")
    private java.lang.Integer warehouseId;
    /**
     * 库位id
     */
    @Excel(name = "库位id", width = 15)
    @ApiModelProperty(value = "库位id")
    private java.lang.Integer storageLocationId;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private java.lang.String projectNo;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;

    /**
     * 物料id
     */
    @Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id")
    private java.lang.Integer materialId;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量", width = 15)
    @ApiModelProperty(value = "库存数量")
    private BigDecimal inventoryQuantity;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 计划id
     */
    @Excel(name = "backup1", width = 15)
    @ApiModelProperty(value = "backup1")
    private java.lang.Integer backup1;
    /**
     * 计划表名
     */
    @Excel(name = "backup2", width = 15)
    @ApiModelProperty(value = "backup2")
    private java.lang.Integer backup2;
    /**
     * 单位
     */
    @Excel(name = "backup3", width = 15)
    @ApiModelProperty(value = "backup3")
    private java.lang.Integer backup3;
    /**
     * backup4
     */
    @Excel(name = "backup4", width = 15)
    @ApiModelProperty(value = "backup4")
    private BigDecimal backup4;
    /**
     * backup5
     * 电缆库存重量
     */
    @Excel(name = "backup5", width = 15)
    @ApiModelProperty(value = "backup5")
    private BigDecimal backup5;
    /**
     * 电缆回收规格
     * liu
     * 2020-08-20
     */
    @Excel(name = "电缆回收规格", width = 15)
    @ApiModelProperty(value = "电缆回收规格")
    private java.lang.String recyclingSpecifications;
    /**
     * 资产编号
     * liu
     * 2020-08-20
     */
    @Excel(name = "资产编号", width = 15)
    @ApiModelProperty(value = "资产编号")
    private java.lang.String assetNo;
}
