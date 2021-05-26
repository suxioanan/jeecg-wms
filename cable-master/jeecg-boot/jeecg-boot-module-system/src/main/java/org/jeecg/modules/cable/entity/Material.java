package org.jeecg.modules.cable.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
 * 物料表
 */
@Data
@TableName("material")
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物料表id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "物料表id")
    private Integer id;
    /**
     * 物料编号(唯一)
     */
    @Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "物料编号")
    private String serial;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private String name;
    /**
     * 规格型号
     */
    @Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private String ations;
    /**
     * 供应商
     */
    @Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String supplier;
    /**
     * 单位(米，吨，卷，台，箱)
     */
    @Excel(name = "单位(字典 米，吨，卷，台，箱)", width = 15)
    @ApiModelProperty(value = "单位(字典 米，吨，卷，台，箱)")
    @Dict(dicCode = "unit")     // 映射字典数据
    private Integer unit;
    /**
     * 物料容积
     */
    @Excel(name = "物料容积", width = 15)
    @ApiModelProperty(value = "物料容积")
    private BigDecimal materialVolume;
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
    private String createBy;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 物料简称 2020/7/14 改
     */
    @Excel(name = "物料简称", width = 15)
    @ApiModelProperty(value = "物料简称")
    private String backup1;
    /**
     * backup2
     */
    @Excel(name = "backup2", width = 15)
    @ApiModelProperty(value = "backup2")
    private String backup2;
    /**
     * backup3
     */
    @Excel(name = "backup3", width = 15)
    @ApiModelProperty(value = "backup3")
    private String backup3;
    /**
     * backup4
     */
    @Excel(name = "backup4", width = 15)
    @ApiModelProperty(value = "backup4")
    private String backup4;
    /**
     * backup5
     */
    @Excel(name = "backup5", width = 15)
    @ApiModelProperty(value = "backup5")
    private String backup5;

    //==================================物料年度出入台账关系映射所需属性

    // 入库表属性
    @TableField(exist = false)
    private DeliverStorage deliverStorage;

    // 出库表属性
    @TableField(exist = false)
    private ReceivingStorage receivingStorage;

    // 计划表1属性
    @TableField(exist = false)
    private Plan1 plan1;
}
