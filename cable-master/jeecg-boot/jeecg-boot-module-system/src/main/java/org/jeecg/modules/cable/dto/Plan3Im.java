package org.jeecg.modules.cable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Plan3Im implements Serializable {
    private static final long serialVersionUID = 3104282117205546755L;

    // 计划表3id
    @Excel(name = "序号", width = 15)
    private Integer id;

    // 工程账号
    @Excel(name = "工程编号", width = 15)
    private String projectNo;

    // 工程名称
    @Excel(name = "工程名称", width = 15)
    private String engName;

    // 采购申请号
    @Excel(name = "采购申请号", width = 15)
    private String proApplyNo;

    // 采购订单号
    @Excel(name = "采购订单号", width = 15)
    private String proTheorderNo;

    // 行项目号
    @Excel(name = "行项目号", width = 15)
    private String lineitemNo;

    // 物料代码
    @Excel(name = "物料代码", width = 15)
    private String materialCode;

    // 物料描述
    @Excel(name = "物料描述", width = 15)
    private String materialDescribe;

    // 计量单位
    @Excel(name = "计量单位", width = 15)
    private String measuringUnit;

    // 供应商
    @Excel(name = "供应商", width = 15)
    private String supplier;

    // 需求数量
    @Excel(name = "需求数量", width = 15)
    private BigDecimal num;

    // 项目经理
    @Excel(name = "项目经理", width = 15)
    private String projectManager;

    // 联系方式
    @Excel(name = "联系方式", width = 15)
    private String mPhone;

    // 施工单位名称
    @Excel(name = "施工单位名称", width = 15)
    private String companyName;

    // 项目现场收货人
    @Excel(name = "项目现场收货人", width = 15)
    private String fieldConsignee;

    // 联系方式
    @Excel(name = "联系方式", width = 15)
    private String cPhone;

    // 送货地点
    @Excel(name = "送货(施工)地点", width = 15)
    private String address;

    // 具体施工日期
    @Excel(name = "具体施工日期", width = 25, format = "yyyy/MM/dd")
    private Date constructionTime;

    // 具体到货日期
    @Excel(name = "具体到货日期", width = 25, format = "yyyy/MM/dd")
    private Date dateOfArrival;

    // 要求最终到货日期
    @Excel(name = "要求最终到货日期", width = 25, format = "yyyy/MM/dd")
    private Date stopTime;

    // 物资调配中心反馈意见(物资供应公司填写
    @Excel(name = "物资调配中心反馈意见(物资供应公司填写)", width = 15)
    private String feedback;

    // 备注
    @Excel(name = "备注", width = 15)
    private String note;

    // 施工单位名称
    @Excel(name = "施工单位名称", width = 15)
    private String constructionOrganization;

    // 说明
    @Excel(name = "说明2", width = 15)
    private String instructions;

    // 总包单位
    @Excel(name = "总包单位", width = 15)
    private String mainContractor;

    // 入库日期
    @Excel(name = "入库日期", width = 25, format = "yyyy/MM/dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverTime;

    // 入库数量
    @Excel(name = "入库数量", width = 15)
    private BigDecimal deliverNum;

    // 出库日期
    @Excel(name = "出库日期", width = 25, format = "yyyy/MM/dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivingTime;

    // 出库数量
    @Excel(name = "出库数量", width = 15)
    private BigDecimal receivingNum;

    @Excel(name = "情况说明", width = 15)
    private String annotation;

    @Excel(name = "反馈日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date feedbackDateTime;
}

