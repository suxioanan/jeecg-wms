package org.jeecg.modules.cable.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车保险表
 */
@Data
@TableName("insurance")
public class Insurance implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 车保险表id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "车保险表id")
    private java.lang.Integer id;
    /**
     * 车牌号码
     */
    @Excel(name = "车牌号码", width = 15)
    @ApiModelProperty(value = "车牌号码")
    private java.lang.String license;
    /**
     * 保险公司
     */
    @Excel(name = "保险公司", width = 15)
    @ApiModelProperty(value = "保险公司")
    private java.lang.String insuraName;
    /**
     * 商业险保单号
     */
    @Excel(name = "商业险保单号", width = 15)
    @ApiModelProperty(value = "商业险保单号")
    private java.lang.String insurancePolicy;
    /**
     * 交强险开始日期
     */
    @Excel(name = "交强险开始日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "交强险开始日期")
    private java.util.Date strongDateBegin;
    /**
     * 交强险结束日期
     */
    @Excel(name = "交强险结束日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "交强险结束日期")
    private java.util.Date strongDateEnd;
    /**
     * 商业险开始日期
     */
    @Excel(name = "商业险开始日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "商业险开始日期")
    private java.util.Date insuranceDateBegin;
    /**
     * 商业险结束日期
     */
    @Excel(name = "商业险结束日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "商业险结束日期")
    private java.util.Date insuranceDateEnd;
    /**
     * 交强险保单号
     */
    @Excel(name = "交强险保单号", width = 15)
    @ApiModelProperty(value = "交强险保单号")
    private java.lang.String strongPolicy;
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
