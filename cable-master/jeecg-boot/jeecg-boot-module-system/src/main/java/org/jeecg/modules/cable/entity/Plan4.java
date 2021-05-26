package org.jeecg.modules.cable.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 计划表4
 */
@Data
@TableName("plan4")
public class Plan4 implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**
     * 计划类型
     */
    @Excel(name = "计划类型", width = 15)
    @ApiModelProperty(value = "计划类型")
    private java.lang.String planType;
    /**
     * 工程名称
     */
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String engName;
    /**
     * 账号
     */
    @Excel(name = "账号", width = 15)
    @ApiModelProperty(value = "账号")
    private java.lang.String projectNo;
    /**
     * 电缆名称
     */
    @Excel(name = "电缆名称", width = 15)
    @ApiModelProperty(value = "电缆名称")
    private java.lang.String cableName;
    /**
     * 电压等级
     */
    @Excel(name = "电压等级", width = 15)
    @ApiModelProperty(value = "电压等级")
    private java.lang.String voltageGrade;
    /**
     * 电缆截面
     */
    @Excel(name = "电缆截面", width = 15)
    @ApiModelProperty(value = "电缆截面")
    private java.lang.String cableCross;
    /**
     * 预计抽取长度
     */
    @Excel(name = "预计抽取长度", width = 15)
    @ApiModelProperty(value = "预计抽取长度")
    private java.lang.String samplingLength;
    /**
     * 抽取日期
     */
    @Excel(name = "抽取日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+08", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "抽取日期")
    private java.util.Date samplingDate;
    /**
     * 抽取地点
     */
    @Excel(name = "抽取地点", width = 15)
    @ApiModelProperty(value = "抽取地点")
    private java.lang.String samplingAddres;
    /**
     * 施工班组
     */
    @Excel(name = "施工班组", width = 15)
    @ApiModelProperty(value = "施工班组")
    private java.lang.String construc;
    /**
     * 班组联系人及电话
     */
    @Excel(name = "班组联系人及电话", width = 15)
    @ApiModelProperty(value = "班组联系人及电话")
    private java.lang.String construcContact;
    /**
     * 施工队伍
     */
    @Excel(name = "施工队伍", width = 15)
    @ApiModelProperty(value = "施工队伍")
    private java.lang.String team;
    /**
     * 队伍联系人及电话
     */
    @Excel(name = "队伍联系人及电话", width = 15)
    @ApiModelProperty(value = "队伍联系人及电话")
    private java.lang.String teamContact;
    /**
     * 专职
     */
    @Excel(name = "专职", width = 15)
    @ApiModelProperty(value = "专职")
    private java.lang.String full;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
    /**
     * 运检反馈
     */
    @Excel(name = "运检反馈", width = 15)
    @ApiModelProperty(value = "运检反馈")
    private java.lang.String feedback;
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
     */
    @Excel(name = "backup2", width = 15)
    @ApiModelProperty(value = "backup2")
    private java.lang.String backup2;
    /**
     * backup3
     */
    @Excel(name = "backup3", width = 15)
    @ApiModelProperty(value = "backup3")
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
}
