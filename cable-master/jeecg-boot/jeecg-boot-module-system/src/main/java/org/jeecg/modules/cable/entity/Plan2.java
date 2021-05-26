package org.jeecg.modules.cable.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 计划表2
 */
@Data
@TableName("plan2")
public class Plan2 implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 计划表2id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "计划表2id")
    private java.lang.Integer id;
    /**
     * 计划类型(字典 备品\临措)
     */
    @Excel(name = "计划类型", width = 15)
    @ApiModelProperty(value = "计划类型")
    private java.lang.String planType;
    /**
     * 站点（老）
     * 工程名称（新）
     * liu
     * 2020-7-22
     */
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String site;
    /**
     * 设备名（老）
     * 物料描述（新）
     */
    @Excel(name = "物料描述", width = 15)
    @ApiModelProperty(value = "物料描述")
    private java.lang.String equipmentName;
    /**
     * 实施工程项目编号（老）
     * 工程账号（新）
     */
    @Excel(name = "工程账号", width = 15)
    @ApiModelProperty(value = "工程账号")
    private java.lang.String projectNo;
    /**
     * 退役设备资产编号
     */
    @Excel(name = "退役设备资产编号", width = 15)
    @ApiModelProperty(value = "退役设备资产编号")
    private java.lang.String retiredAssetNo;
    /**
     * 容量
     */
    @Excel(name = "容量", width = 15)
    @ApiModelProperty(value = "容量")
    private java.lang.String capacity;
    /**
     * 型号
     */
    @Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
    private java.lang.String model;
    /**
     * ERP现资产状态
     */
    @Excel(name = "ERP现资产状态", width = 15)
    @ApiModelProperty(value = "ERP现资产状态")
    private java.lang.String assetStatus;
    /**
     * 退役时间
     */
    @Excel(name = "退役时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "退役时间")
    private java.util.Date retiredDate;
    /**
     * 是否有入库单
     */
    @Excel(name = "是否有入库单", width = 15)
    @ApiModelProperty(value = "是否有入库单")
    private java.lang.String receiptIs;
    /**
     * 库存地点
     */
    @Excel(name = "库存地点", width = 15)
    @ApiModelProperty(value = "库存地点")
    private java.lang.String theLocation;
    /**
     * 处置方式
     */
    @Excel(name = "处置方式", width = 15)
    @ApiModelProperty(value = "处置方式")
    private java.lang.String disposalWay;
    /**
     * 新资产编号
     */
    @Excel(name = "新资产编号", width = 15)
    @ApiModelProperty(value = "新资产编号")
    private java.lang.String assetNo;
    /**
     * 项目分类
     */
    @Excel(name = "项目分类", width = 15)
    @ApiModelProperty(value = "项目分类")
    private java.lang.String projectType;
    /**
     * 设备主人
     */
    @Excel(name = "设备主人", width = 15)
    @ApiModelProperty(value = "设备主人")
    private java.lang.String equipmentOwners;
    /**
     * 地址
     */
    @Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private java.lang.String address;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
    /**
     * 设备号
     */
    @Excel(name = "设备号", width = 15)
    @ApiModelProperty(value = "设备号")
    private java.lang.String equipmentNo;
    /**
     * 实物已退役但未处置
     */
    @Excel(name = "实物已退役但未处置", width = 15)
    @ApiModelProperty(value = "实物已退役但未处置")
    private java.lang.String disposed;
    /**
     * 入库单号（老）
     * 物料代码（新）
     */
    @Excel(name = "物料代码", width = 15)
    @ApiModelProperty(value = "物料代码")
    private java.lang.String receiptNo;
    /**
     * 已入库数
     */
    @Excel(name = "已入库数", width = 15)
    @ApiModelProperty(value = "已入库数")
    private java.math.BigDecimal alreadyDeliverStorage;
    /**
     * 已出库数
     */
    @Excel(name = "已出库数", width = 15)
    @ApiModelProperty(value = "已出库数")
    private java.math.BigDecimal alreadyReceivingStorage;
    /**
     * 0未派单/1已派单
     */
    @Excel(name = "0未派单/1已派单", width = 15)
    @ApiModelProperty(value = "0未派单/1已派单")
    private java.lang.Integer sendOrdersState;
    /**
     * 完成状态(字典)
     */
    @Excel(name = "完成状态(字典)", width = 15)
    @ApiModelProperty(value = "完成状态(字典)")
    @Dict(dicCode = "completeState")
    private java.lang.Integer completeState;
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
     * backup1
     */
    @Excel(name = "backup1", width = 15)
    @ApiModelProperty(value = "backup1")
    private java.lang.String backup1;
    /**
     * backup2
     * 物料描述
     */
    @Excel(name = "物料描述", width = 15)
    @ApiModelProperty(value = "物料描述")
    private java.lang.String backup2;
    /**
     * backup3
     * 物料代码
     */
    @Excel(name = "物料代码", width = 15)
    @ApiModelProperty(value = "物料代码")
    private java.lang.String backup3;
    /**
     * backup4
     */
    @Excel(name = "backup4", width = 15)
    @ApiModelProperty(value = "backup4")
    private java.lang.String backup4;
    /**
     * backup5
     */
    @Excel(name = "backup5", width = 15)
    @ApiModelProperty(value = "backup5")
    private java.lang.String backup5;

    /**
     * 导出时选择计划开始日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)  //exist = false 是否为数据库表字段
    private Date beginTime;

    /**
     * 导出时选择计划结束日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)  //exist = false  是否为数据库表字段
    private Date endTime;
}
