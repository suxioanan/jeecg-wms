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
 * 出库/完单表
 */
@Data
@TableName("receiving_storage")
public class ReceivingStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "计划类型(区分计划1\2\3\4表)")
    private Integer planType;

    @ApiModelProperty(value = "计划id")
    private Integer planId;

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

    /*回收价格*/
    @Excel(name = "回收价格", width = 15)
    @ApiModelProperty(value = "回收价格")
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

    /**
     * 出库数量
     */
    @Excel(name = "出库数量", width = 15)
    @ApiModelProperty(value = "出库数量")
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

    /*完单状态*/
    @Excel(name = "完单状态", width = 15)
    @ApiModelProperty(value = "完单状态")
    private Integer state;

    /**
     * 情况
     */
    @Excel(name = "情况", width = 15)
    @ApiModelProperty(value = "情况")
    private Integer sceneSituation;

    /**
     * 现场异常照片路径(路径用逗号隔开)
     */
    @Excel(name = "现场异常照片路径(路径用逗号隔开)", width = 15)
    @ApiModelProperty(value = "现场异常照片路径(路径用逗号隔开)")
    private String sceneAbnormalPhotos;

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
     * 出库日期
     */
    @Excel(name = "出库日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "出库日期")
    private java.util.Date receivingTime;

    /**
     * 情况说明
     */
    @Excel(name = "情况说明", width = 15)
    @ApiModelProperty(value = "情况说明")
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
     * 终点仓库id
     */
    @Excel(name = "终点仓库id", width = 15)
    @ApiModelProperty(value = "终点仓库id")
    private Integer backup1;
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

    // 1月份出库数量
    @Excel(name = "m1CK", width = 15)
    @ApiModelProperty(value = "m1CK")
    @TableField(exist = false)
    private Integer m1CK;

    // 2月份出库数量
    @Excel(name = "m2CK", width = 15)
    @ApiModelProperty(value = "m2CK")
    @TableField(exist = false)
    private Integer m2CK;

    // 3月份出库数量
    @Excel(name = "m3CK", width = 15)
    @ApiModelProperty(value = "m3CK")
    @TableField(exist = false)
    private Integer m3CK;

    // 4月份出库数量
    @Excel(name = "m4CK", width = 15)
    @ApiModelProperty(value = "m4CK")
    @TableField(exist = false)
    private Integer m4CK;

    // 5月份出库数量
    @Excel(name = "m5CK", width = 15)
    @ApiModelProperty(value = "m5CK")
    @TableField(exist = false)
    private Integer m5CK;

    // 6月份出库数量
    @Excel(name = "m6CK", width = 15)
    @ApiModelProperty(value = "m6CK")
    @TableField(exist = false)
    private Integer m6CK;

    // 7月份出库数量
    @Excel(name = "m7CK", width = 15)
    @ApiModelProperty(value = "m7CK")
    @TableField(exist = false)
    private Integer m7CK;

    // 8月份出库数量
    @Excel(name = "m8CK", width = 15)
    @ApiModelProperty(value = "m8CK")
    @TableField(exist = false)
    private Integer m8CK;

    // 9月份出库数量
    @Excel(name = "m9CK", width = 15)
    @ApiModelProperty(value = "m9CK")
    @TableField(exist = false)
    private Integer m9CK;

    // 10月份出库数量
    @Excel(name = "m10CK", width = 15)
    @ApiModelProperty(value = "m10CK")
    @TableField(exist = false)
    private Integer m10CK;

    // 11月份出库数量
    @Excel(name = "m11CK", width = 15)
    @ApiModelProperty(value = "m11CK")
    @TableField(exist = false)
    private Integer m11CK;

    // 12月份出库数量
    @Excel(name = "m12CK", width = 15)
    @ApiModelProperty(value = "m12CK")
    @TableField(exist = false)
    private Integer m12CK;

    // 全年出库总数量
    @Excel(name = "CKnums", width = 15)
    @ApiModelProperty(value = "CKnums")
    @TableField(exist = false)
    private Integer CKnums;
}
