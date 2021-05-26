package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Plan4Vo implements Serializable {
    private static final long serialVersionUID = 1931889797509480354L;


    // 开始时间
//    @Excel(name = "开始时间", width = 15)
    private String beginTime;

    // 截止时间
//    @Excel(name = "截止时间", width = 15)
    private String endTime;

    // 工程账号
    @Excel(name = "工程账号", width = 15)
    private String projectNo;

    // 电缆名称
    @Excel(name = "电缆名称", width = 15)
    private String cableName;

    // 电压等级
//    @Excel(name = "电压等级", width = 15)
    private String voltageGrade;

    // 电缆截面
//    @Excel(name = "电缆截面", width = 15)
    private String cableCross;

    //回收规格
    @Excel(name = "回收规格", width = 15)
    private String recyclingSpecifications;

    // 材质
    @Excel(name = "材质", width = 15)
    private String texture;

    // 长度(M)
    @Excel(name = "入库长度(M)", width = 15)
    private String length;

    // 重量(吨)
    @Excel(name = "入库重量(吨)", width = 15)
    private String weight;

    // 完单数量
//    @Excel(name = "完单数量", width = 15)
    private BigDecimal accomplishNum;

    // 完单重量
//    @Excel(name = "完单重量", width = 15)
    private BigDecimal accomplishWeight;

    // 起止时间
    @Excel(name = "起始时间", width = 15)
    private String buildTime;

    // 退役日期
    @Excel(name = "退役日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date decommissioningDate;

    // 情况说明
    @Excel(name = "情况说明", width = 15)
    private String description;

    // 交接单号
    @Excel(name = "交接单号", width = 15)
    private String itemSlip;

    // 备注
    @Excel(name = "备注", width = 15)
    private String remark;

    // 资产编号
    @Excel(name = "资产编号", width = 15)
    private String assetNo;

    // 设备号
    @Excel(name = "设备号", width = 15)
    private String equipmentDeveui;

    // 测绘长度(M)
    @Excel(name = "测绘长度(M)", width = 15)
    private String mappingLength;

    // 足量情况
    @Excel(name = "足量情况", width = 15)
    private String sufficiency;

    // 报废流程节点
    @Excel(name = "报废流程节点", width = 15)
    private String scrapProcessNode;

    /**
     * 项目名称
     */
    private String engName;
    /**
     * 仓库id
     */
    private Integer warehouseId;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 库位id
     */
    private Integer storageLocationId;
    /**
     * 库位名称
     */
    private String storageLocationName;
    /**
     * 可出库数量[批量完单操作时使用此属性] ----2020/8/26 bai
     */
    private BigDecimal inventoryQuantity;
    /**
     * 批量完单的计划 id集合
     */
    private String plan4Ids;
    /**
     * 完单类型[0:出库\1:入库]
     */
    private String operatorSchema;
    /**
     * 交接单号
     */
    private String plan4ReceiptNo;
    /**
     * 回单照片
     */
    private String receiptPhotos;
    /**
     * 任务日期
     */
    private String taskTime;
    /**
     * 出库重量
     */
    private String retrievalWeight;
    /**
     * 合并完单填写的数据集
     */
    private List<?> completeOrderList;
}
