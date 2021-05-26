package org.jeecg.modules.cable.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 入库/完单表
 */
@Data
@TableName("deliver_storage")
public class DeliverStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    private Integer planId;

    private Integer planType;

    /**
     * 派单id
     */
    @Excel(name = "派单id", width = 15)
    @ApiModelProperty(value = "派单id")
    private Integer sendOrdersId;

    /**
     * 物料id
     */
    @Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id")
    private Integer materialId;

    /**
     * 仓库id
     */
    @Excel(name = "仓库id", width = 15)
    @ApiModelProperty(value = "仓库id")
    private Integer warehouseId;

    /**
     * 库位id
     */
    @Excel(name = "库位id", width = 15)
    @ApiModelProperty(value = "库位id")
    private Integer storageLocationId;

    /**
     * 完单数量
     */
    @Excel(name = "完单数量", width = 15)
    @ApiModelProperty(value = "完单数量")
    private java.math.BigDecimal accomplishNum;

    /**
     * 完单数量单位（字典）
     */
    @Excel(name = "完单数量单位（字典）", width = 15)
    @ApiModelProperty(value = "完单数量单位（字典）")
    private Integer accomplishNumUnit;

    /**
     * 完单重量
     */
    @Excel(name = "完单重量", width = 15)
    @ApiModelProperty(value = "完单重量")
    private java.math.BigDecimal accomplishWeight;

    /**
     * 完单重量完单重量（字典）
     */
    @Excel(name = "完单重量完单重量（字典）", width = 15)
    @ApiModelProperty(value = "完单重量完单重量（字典）")
    private Integer accomplishWeightUnit;

    /**
     * 完单容积
     */
    @Excel(name = "完单容积", width = 15)
    @ApiModelProperty(value = "完单容积")
    private java.math.BigDecimal accomplishVolume;

    /*回收规格*/
    @Excel(name = "回收规格", width = 15)
    @ApiModelProperty(value = "回收规格")
    private String recyclingSpecifications;

    /*材质（定死，铜，铝）*/
    @Excel(name = "材质（定死，铜，铝）", width = 15)
    @ApiModelProperty(value = "材质（定死，铜，铝）")
    private String texture;

    /*回收情况*/
    @Excel(name = "回收情况", width = 15)
    @ApiModelProperty(value = "回收情况")
    private String recyclingSituation;

    /*是否完成*/
    @Excel(name = "是否完成", width = 15)
    @ApiModelProperty(value = "是否完成")
    private String whetherComplete;

    /*入库交接单号*/
    @Excel(name = "入库交接单号", width = 15)
    @ApiModelProperty(value = "入库交接单号")
    private String receiptNo;

    /*未完成说明*/
    @Excel(name = "未完成说明", width = 15)
    @ApiModelProperty(value = "未完成说明")
    private String incompleteDescription;

    /*完单状态*/
    @Excel(name = "完单状态", width = 15)
    @ApiModelProperty(value = "完单状态")
    private Integer state;

    /**
     * 是否异常(字典0正常 1异常)
     */
    @Excel(name = "是否异常(字典0正常 1异常)", width = 15)
    @ApiModelProperty(value = "是否异常(字典0正常 1异常)")
    private Integer sceneSituation;

    /**
     * 异常原因
     */
    @Excel(name = "异常原因", width = 15)
    @ApiModelProperty(value = "异常原因")
    private String anomalousCause;

    /**
     * 现场照片路径(路径用逗号隔开)
     */
    @Excel(name = "现场照片路径(路径用逗号隔开)", width = 15)
    @ApiModelProperty(value = "现场照片路径(路径用逗号隔开)")
    private String scenePhotos;

    /**
     * 回单照片路径(路径用逗号隔开)
     */
    @Excel(name = "回单照片路径(路径用逗号隔开)", width = 15)
    @ApiModelProperty(value = "回单照片路径(路径用逗号隔开)")
    private String receiptPhotos;

    /**
     * 施工队
     */
    @Excel(name = "施工队", width = 15)
    @ApiModelProperty(value = "施工队")
    private String constructionTeam;

    /**
     * 联系人
     */
    @Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private String linkman;

    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 入库日期
     */
    @Excel(name = "入库日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入库日期")
    private java.util.Date deliverTime;

    /**
     * 说明
     */
    @Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private String annotation;

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
     * backup1
     */
    @Excel(name = "backup1", width = 15)
    @ApiModelProperty(value = "backup1")
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

    //==============================物料年度出入台账查询———映射所需属性

    // 1月份入库数量
    @Excel(name = "m1RK", width = 15)
    @ApiModelProperty(value = "m1RK")
    @TableField(exist = false)
    private Integer m1RK;

    // 2月份入库数量
    @Excel(name = "m2RK", width = 15)
    @ApiModelProperty(value = "m2RK")
    @TableField(exist = false)
    private Integer m2RK;

    // 3月份入库数量
    @Excel(name = "m3RK", width = 15)
    @ApiModelProperty(value = "m3RK")
    @TableField(exist = false)
    private Integer m3RK;

    // 4月份入库数量
    @Excel(name = "m4RK", width = 15)
    @ApiModelProperty(value = "m4RK")
    @TableField(exist = false)
    private Integer m4RK;

    // 5月份入库数量
    @Excel(name = "m5RK", width = 15)
    @ApiModelProperty(value = "m5RK")
    @TableField(exist = false)
    private Integer m5RK;

    // 6月份入库数量
    @Excel(name = "m6RK", width = 15)
    @ApiModelProperty(value = "m6RK")
    @TableField(exist = false)
    private Integer m6RK;

    // 7月份入库数量
    @Excel(name = "m7RK", width = 15)
    @ApiModelProperty(value = "m7RK")
    @TableField(exist = false)
    private Integer m7RK;

    // 8月份入库数量
    @Excel(name = "m8RK", width = 15)
    @ApiModelProperty(value = "m8RK")
    @TableField(exist = false)
    private Integer m8RK;

    // 9月份入库数量
    @Excel(name = "m9RK", width = 15)
    @ApiModelProperty(value = "m9RK")
    @TableField(exist = false)
    private Integer m9RK;

    // 10月份入库数量
    @Excel(name = "m10RK", width = 15)
    @ApiModelProperty(value = "m10RK")
    @TableField(exist = false)
    private Integer m10RK;

    // 11月份入库数量
    @Excel(name = "m11RK", width = 15)
    @ApiModelProperty(value = "m11RK")
    @TableField(exist = false)
    private Integer m11RK;

    // 12月份入库数量
    @Excel(name = "m12RK", width = 15)
    @ApiModelProperty(value = "m12RK")
    @TableField(exist = false)
    private Integer m12RK;

    // 全年入库总数量
    @Excel(name = "RKnums", width = 15)
    @ApiModelProperty(value = "RKnums")
    @TableField(exist = false)
    private Integer RKnums;
}
