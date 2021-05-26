package org.jeecg.modules.cable.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 派单表
 */
@Data
@TableName("send_orders")
public class SendOrders implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 派单表id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "派单表id")
    private java.lang.Integer id;
    /**
     * 操作模式（字典：0.出库，1.入库）
     */
    @Excel(name = "操作模式（字典：0.出库，1.入库）", width = 15)
    @ApiModelProperty(value = "操作模式（字典：0.出库，1.入库）")
    private java.lang.Integer operatorSchema;
    /**
     * 仓库id
     */
    @Excel(name = "仓库id", width = 15)
    @ApiModelProperty(value = "仓库id")
    private java.lang.Integer warehouseId;
    /**
     * 终点仓库id
     */
    @Excel(name = "终点仓库id", width = 15)
    @ApiModelProperty(value = "终点仓库id")
    private java.lang.Integer endWarehouseId;
    /**
     * 出库时要选库位 库位要根据项目编号/工程编号来
     */
    @Excel(name = "出库时要选库位 库位要根据项目编号/工程编号来", width = 15)
    @ApiModelProperty(value = "出库时要选库位 库位要根据项目编号/工程编号来")
    private java.lang.Integer storageLocationId;
    /**
     * 计划id
     */
    @Excel(name = "计划id", width = 15)
    @ApiModelProperty(value = "计划id")
    private java.lang.Integer planId;
    /**
     * 区分计划1/2/3/4表
     */
    @Excel(name = "区分计划1/2/3/4表", width = 15)
    @ApiModelProperty(value = "区分计划1/2/3/4表")
    private java.lang.String planType;
    /**
     * 任务时间
     */
    @Excel(name = "任务时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "任务时间")
    private java.util.Date taskTime;
    /**
     * 项目编码
     */
    @Excel(name = "项目编码", width = 15)
    @ApiModelProperty(value = "项目编码")
    private java.lang.String projectNo;
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
     * 派单数量
     */
    @Excel(name = "backup1", width = 15)
    @ApiModelProperty(value = "backup1")
    private BigDecimal backup1;
    /**
     * backup2
     * 任务地址
     */
    @Excel(name = "backup2", width = 15)
    @ApiModelProperty(value = "backup2")
    private java.lang.String backup2;
    /**
     * backup3
     * 联系人
     */
    @Excel(name = "backup3", width = 15)
    @ApiModelProperty(value = "backup3")
    private java.lang.String backup3;
    /**
     * backup4
     * 电话
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
}
