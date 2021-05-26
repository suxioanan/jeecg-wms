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
 * 计划表1
 */
@Data
@TableName("plan1")
public class Plan1 implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 计划表1id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "计划表1id")
    private java.lang.Integer id;
    /**
     * 0.配变电1.其他、2.电缆1、3.线路 导入界面及新增界面下拉 写死
     */
    @Excel(name = "配变电、其他、电缆1、线路", width = 15)
    @ApiModelProperty(value = "配变电、其他、电缆1、线路")
    private java.lang.String planType;
    /**
     * 项目类型
     */
    @Excel(name = "项目类型", width = 15)
    @ApiModelProperty(value = "项目类型")
    private java.lang.String projectType;
    /**
     * 项目交接单
     */
    @Excel(name = "交接单行号", width = 15)
    @ApiModelProperty(value = "交接单行号")
    private java.lang.String itemSlipNo;
    /**
     * 项目交接单
     */
    @Excel(name = "项目交接单", width = 15)
    @ApiModelProperty(value = "项目交接单")
    private java.lang.String itemSlip;
    /**
     * 交接单类型
     */
    @Excel(name = "交接单类型", width = 15)
    @ApiModelProperty(value = "交接单类型")
    private java.lang.String itemSlipType;
    /**
     * 业务类型
     */
    @Excel(name = "业务类型", width = 15)
    @ApiModelProperty(value = "业务类型")
    private java.lang.String businessType;
    /**
     * 所属工厂
     */
    @Excel(name = "所属工厂", width = 15)
    @ApiModelProperty(value = "所属工厂")
    private java.lang.String theFactory;
    /**
     * 工厂描述
     */
    @Excel(name = "工厂描述", width = 15)
    @ApiModelProperty(value = "工厂描述")
    private java.lang.String factoryText;
    /**
     * 处置方式
     */
    @Excel(name = "处置方式", width = 15)
    @ApiModelProperty(value = "处置方式")
    private java.lang.String disposalWay;
    /**
     * 库存地点
     */
    @Excel(name = "库存地点", width = 15)
    @ApiModelProperty(value = "库存地点")
    private java.lang.String theLocation;
    /**
     * 库存点描述
     */
    @Excel(name = "库存点描述", width = 15)
    @ApiModelProperty(value = "库存点描述")
    private java.lang.String theLocationText;
    /**
     * 原物料编码(从字典来)
     */
    @Excel(name = "原物料编码(从字典来)", width = 15)
    @ApiModelProperty(value = "原物料编码(从字典来)")
    private java.lang.String rawMaterialCode;
    /**
     * 原物料描述
     */
    @Excel(name = "原物料描述", width = 15)
    @ApiModelProperty(value = "原物料描述")
    private java.lang.String rawMaterialText;
    /**
     * 原物料单位(从字典来)
     */
    @Excel(name = "原物料单位(从字典来)", width = 15)
    @ApiModelProperty(value = "原物料单位(从字典来)")
    private java.lang.Integer rawMaterialUnit;
    /**
     * 废旧物料编码(从字典来)
     */
    @Excel(name = "废旧物料编码(从字典来)", width = 15)
    @ApiModelProperty(value = "废旧物料编码(从字典来)")
    private java.lang.String wasteMaterialCode;
    /**
     * 废旧物料描述
     */
    @Excel(name = "废旧物料描述", width = 15)
    @ApiModelProperty(value = "废旧物料描述")
    private java.lang.String wasteMaterialText;
    /**
     * 废旧物料单位(从字典来)
     */
    @Excel(name = "废旧物料单位(从字典来)", width = 15)
    @ApiModelProperty(value = "废旧物料单位(从字典来)")
    private java.lang.Integer wasteMaterialUnit;
    /**
     * 交接单数量(计划出/入)
     */
    @Excel(name = "交接单数量(计划出/入)", width = 15)
    @ApiModelProperty(value = "交接单数量(计划出/入)")
    private java.math.BigDecimal numReceipts;
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
     * 实际入库数量
     */
    @Excel(name = "实际入库数量", width = 15)
    @ApiModelProperty(value = "实际入库数量")
    private java.math.BigDecimal deliverNum;
    /**
     * 入库日期
     */
    @Excel(name = "入库日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入库日期")
    private java.util.Date deliverTime;
    /**
     * 入库凭证
     */
    @Excel(name = "入库凭证", width = 15)
    @ApiModelProperty(value = "入库凭证")
    private java.lang.String deliverVoucher;
    /**
     * 项目编码(入库的时候需要把项目编码带到仓库)
     */
    @Excel(name = "项目编码(入库的时候需要把项目编码带到仓库)", width = 15)
    @ApiModelProperty(value = "项目编码(入库的时候需要把项目编码带到仓库)")
    private java.lang.String projectNo;
    /**
     * 项目名称(入库的时候需要把项目名称带到仓库)
     */
    @Excel(name = "项目名称(入库的时候需要把项目名称带到仓库)", width = 15)
    @ApiModelProperty(value = "项目名称(入库的时候需要把项目名称带到仓库)")
    private java.lang.String projectName;
    /**
     * 资产编码
     */
    @Excel(name = "资产编码", width = 15)
    @ApiModelProperty(value = "资产编码")
    private java.lang.String assetNo;
    /**
     * 资产名称
     */
    @Excel(name = "资产名称", width = 15)
    @ApiModelProperty(value = "资产名称")
    private java.lang.String assetText;
    /**
     * 实物ID
     */
    @Excel(name = "实物ID", width = 15)
    @ApiModelProperty(value = "实物ID")
    private java.lang.String realId;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String equipmentDeveui;
    /**
     * 技术对象说明
     */
    @Excel(name = "技术对象说明", width = 15)
    @ApiModelProperty(value = "技术对象说明")
    private java.lang.String objectInstruct;
    /**
     * 制造商型号
     */
    @Excel(name = "制造商型号", width = 15)
    @ApiModelProperty(value = "制造商型号")
    private java.lang.String manufacturing;
    /**
     * 出厂编号
     */
    @Excel(name = "出厂编号", width = 15)
    @ApiModelProperty(value = "出厂编号")
    private java.lang.String factoryNo;
    /**
     * 报废原值
     */
    @Excel(name = "报废原值", width = 15)
    @ApiModelProperty(value = "报废原值")
    private java.lang.String wasteVal;
    /**
     * 报废理由
     */
    @Excel(name = "报废理由", width = 15)
    @ApiModelProperty(value = "报废理由")
    private java.lang.String wasteReason;
    /**
     * 入库交接单创建人
     */
    @Excel(name = "入库交接单创建人", width = 15)
    @ApiModelProperty(value = "入库交接单创建人")
    private java.lang.String deliveryCreateBy;
    /**
     * 入库交接单创建日期
     */
    @Excel(name = "入库交接单创建日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入库交接单创建日期")
    private java.util.Date deliveryCreateTime;
    /**
     * 入库交接单接收日期
     */
    @Excel(name = "入库交接单接收日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入库交接单接收日期")
    private java.util.Date deliveryReceiveTime;
    /**
     * 项目联系人
     */
    @Excel(name = "项目联系人", width = 15)
    @ApiModelProperty(value = "项目联系人")
    private java.lang.String projectContact;
    /**
     * 项目联系电话
     */
    @Excel(name = "项目联系电话", width = 15)
    @ApiModelProperty(value = "项目联系电话")
    private java.lang.String projectPhone;
    /**
     * 删除标识
     */
    @Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    private java.lang.String deleteLogo;
    /**
     * 设计确认拆除数量
     */
    @Excel(name = "设计确认拆除数量", width = 15)
    @ApiModelProperty(value = "设计确认拆除数量")
    private java.lang.String designNum;
    /**
     * 现场联系
     */
    @Excel(name = "现场联系", width = 15)
    @ApiModelProperty(value = "现场联系")
    private java.lang.String theContact;
    /**
     * 现场联系电话
     */
    @Excel(name = "现场联系电话", width = 15)
    @ApiModelProperty(value = "现场联系电话")
    private java.lang.String thePhone;
    /**
     * 仓库联系人
     */
    @Excel(name = "仓库联系人", width = 15)
    @ApiModelProperty(value = "仓库联系人")
    private java.lang.String warehouseContact;
    /**
     * 仓库联系电话
     */
    @Excel(name = "仓库联系电话", width = 15)
    @ApiModelProperty(value = "仓库联系电话")
    private java.lang.String warehousePhone;
    /**
     * 拆除计划
     */
    @Excel(name = "拆除计划", width = 15)
    @ApiModelProperty(value = "拆除计划")
    private java.lang.String demolitionPlan;
    /**
     * 工程详细地址
     */
    @Excel(name = "工程详细地址", width = 15)
    @ApiModelProperty(value = "工程详细地址")
    private java.lang.String engineeringAddress;
    /**
     * 系统系列号
     */
    @Excel(name = "系统系列号", width = 15)
    @ApiModelProperty(value = "系统系列号")
    private java.lang.String systemNo;
    /**
     * 入库或冲销备注
     */
    @Excel(name = "入库或冲销备注", width = 15)
    @ApiModelProperty(value = "入库或冲销备注")
    private java.lang.String noteInformation;
    /**
     * 项目编码备注
     */
    @Excel(name = "项目编码备注", width = 15)
    @ApiModelProperty(value = "项目编码备注")
    private java.lang.String projectNoRemarks;
    /**
     * 资产成新率
     */
    @Excel(name = "资产成新率", width = 15)
    @ApiModelProperty(value = "资产成新率")
    private java.lang.String rateFormation;
    /**
     * 计划报废完成时间
     */
    @Excel(name = "计划报废完成时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "计划报废完成时间")
    private java.util.Date plansCompleteTime;
    /**
     * 0未派单/1已派单
     */
    @Excel(name = "0未派单/1已派单", width = 15)
    @ApiModelProperty(value = "0未派单/1已派单")
    private java.lang.Integer sendOrdersState;
    /**
     * 计划完成状态(字典 0未完成 1已完成)
     */
    @Excel(name = "计划完成状态(字典 0未完成 1已完成)", width = 15)
    @ApiModelProperty(value = "计划完成状态(字典 0未完成 1已完成)")
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
