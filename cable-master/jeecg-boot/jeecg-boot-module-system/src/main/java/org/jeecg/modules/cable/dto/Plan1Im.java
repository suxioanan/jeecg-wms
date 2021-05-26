package org.jeecg.modules.cable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Plan1Im implements Serializable {
    private static final long serialVersionUID = -6285711128150523778L;

    // 主键id
    private Integer id;

    /*入库交接单号*/
    @Excel(name = "入库交接单号", width = 15)
    private String itemSlip;

    /*交接单行号*/
    @Excel(name = "交接单行号", width = 15)
    private String itemSlipNo;

    /*交接单类型*/
    @Excel(name = "交接单类型", width = 15)
    private String itemSlipType;

    /*业务类型*/
    @Excel(name = "业务类型", width = 15)
    private String businessType;

    /*删除标识*/
    @Excel(name = "删除标识", width = 15)
    private String deleteLogo;

    /*所属工厂*/
    @Excel(name = "所属工厂", width = 15)
    private String theFactory;

    /*工厂描述*/
    @Excel(name = "工厂描述", width = 15)
    private String factoryText;

    /*处置方式*/
    @Excel(name = "处置方式", width = 15)
    private String disposalWay;

    /*库存地点*/
    @Excel(name = "库存地点", width = 15)
    private String theLocation;

    /*库存地点描述*/
    @Excel(name = "库存地点描述", width = 15)
    private String theLocationText;

    /*原物料编码*/
    @Excel(name = "原物料编码", width = 15)
    private String rawMaterialCode;

    /*原物料描述*/
    @Excel(name = "原物料描述", width = 15)
    private String rawMaterialText;

    /*设计确认拆除数量*/
    @Excel(name = "设计确认拆除数量", width = 15)
    private String designNum;

    /*单位*/
    @Excel(name = "单位", width = 15)
    private String rawMaterialUnit;

    /*废旧物料编码*/
    @Excel(name = "废旧物料编码", width = 15)
    private String wasteMaterialCode;

    /*废旧物料描述*/
    @Excel(name = "废旧物料描述", width = 15)
    private String wasteMaterialText;

    /*废旧物料单位*/
    @Excel(name = "废旧物料单位", width = 15)
    private String wasteMaterialUnit;

    /*交接单数量*/
    @Excel(name = "交接单数量", width = 15)
    private BigDecimal numReceipts;

    /*实际入库数量*/
    @Excel(name = "实际入库数量", width = 15)
    private BigDecimal deliverNum;

    /*入库凭证*/
    @Excel(name = "入库凭证", width = 15)
    private String deliverVoucher;

    /*项目编码*/
    @Excel(name = "项目编码", width = 15)
    private String projectNo;

    /*项目名称*/
    @Excel(name = "项目名称", width = 15)
    private String projectName;

    /*资产编码*/
    @Excel(name = "资产编码", width = 15)
    private String assetNo;

    /*资产名称*/
    @Excel(name = "资产名称", width = 15)
    private String assetText;

    /*实物ID*/
    @Excel(name = "实物ID", width = 15)
    private String realId;

    /*设备编号*/
    @Excel(name = "设备编号", width = 15)
    private String equipmentDeveui;

    /*技术对象说明*/
    @Excel(name = "技术对象说明", width = 15)
    private String objectInstruct;

    /*制造商型号*/
    @Excel(name = "制造商型号", width = 15)
    private String manufacturing;

    /*出厂编号*/
    @Excel(name = "出厂编号", width = 15)
    private String factoryNo;

    /*报废原值*/
    @Excel(name = "报废原值", width = 15)
    private String wasteVal;

    /*报废理由*/
    @Excel(name = "报废理由", width = 15)
    private String wasteReason;

    /*入库交接单创建人*/
    @Excel(name = "入库交接单创建人", width = 15)
    private String deliveryCreateBy;

    /*入库交接单创建日期*/
    @Excel(name = "入库交接单创建日期", width = 25, format = "yyyy/MM/dd")
    private Date deliveryCreateTime;

    /*入库交接单接收日期*/
    @Excel(name = "入库交接单接收日期", width = 25, format = "yyyy/MM/dd")
    private Date deliveryReceiveTime;

    /*项目（管理）单位联系人*/
    @Excel(name = "项目(管理)单位联系人", width = 15)
    private String projectContact;

    /*项目（管理）单位联系电话*/
    @Excel(name = "项目(管理)单位联系电话", width = 15)
    private String projectPhone;

    /*现场联系人*/
    @Excel(name = "现场联系人", width = 15)
    private String theContact;

    /*现场联系电话*/
    @Excel(name = "现场联系电话", width = 15)
    private String thePhone;

    /*仓库联系人*/
    @Excel(name = "仓库联系人", width = 15)
    private String warehouseContact;

    /*现场拆除计划及说明*/
    @Excel(name = "现场拆除计划及说明", width = 15)
    private String demolitionPlan;

    /*工程详细地址*/
    @Excel(name = "工程详细地址", width = 15)
    private String engineeringAddress;

    /*系统排序号*/
    @Excel(name = "系统排序号", width = 15)
    private String systemNo;

    /*入库或冲销备注*/
    @Excel(name = "入库或冲销备注", width = 15)
    private String noteInformation;

    /*项目编码备注*/
    @Excel(name = "项目编码备注", width = 15)
    private String projectNoRemarks;

    /*资产成新率*/
    @Excel(name = "资产成新率", width = 15)
    private String rateFormation;

    /*计划报废完成时间*/
    @Excel(name = "计划报废完成时间", width = 25, format = "yyyy/MM/dd")
    private Date plansCompleteTime;

    // 项目类型
    @Excel(name = "项目类型", width = 15)
    private String projectType;

    // 导入计划类型
    @Excel(name = "项目类型", width = 15)
    private String planType;

    // 入库数量
    @Excel(name = "入库数量", width = 15)
    private BigDecimal stockNum;

    /*入库日期*/
    @Excel(name = "入库日期", width = 25, format = "yyyy/MM/dd")
    private Date deliverTime;

    // 库存点
    @Excel(name = "中间点", width = 15)
    private String warehouseName;

    // 出库数量
    @Excel(name = "出库数量", width = 15)
    private BigDecimal receivingNum;

    // 出库日期
    @Excel(name = "出库日期", width = 25, format = "yyyy/MM/dd")
    private Date receivingTime;

    // 终点仓
    @Excel(name = "终点仓", width = 15)
    private String terminalBin;

    // 反馈日期
    @Excel(name = "反馈日期", width = 25, format = "yyyy/MM/dd")
    private Date feedbackDate;

    // 补充说明
    @Excel(name = "补充说明", width = 15)
    private String explain;

    /*计划完成状态*/
    private String completeState;

    /**
     * 导出时选择计划开始日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 导出时选择计划结束日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
