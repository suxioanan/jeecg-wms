package org.jeecg.modules.cable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Plan2Im implements Serializable {
    private static final long serialVersionUID = -1425320488905440913L;
    /**
     * 计划表2id
     */
    @Excel(name = "序号", width = 15)
    private Integer id;
    /**
     * 工程名称
     */
    @Excel(name = "工程名称", width = 15)
    private String site;
    /**
     * 设备名
     */
    @Excel(name = "设备名", width = 15)
    private String equipmentName;
    /**
     * 工程账号（新）
     */
    @Excel(name = "工程账号", width = 15)
    private String projectNo;
    /**
     * 退役设备资产编号
     */
    @Excel(name = "退役设备资产编号", width = 15)
    private String retiredAssetNo;
    /**
     * 容量
     */
    @Excel(name = "容量", width = 15)
    private String capacity;
    /**
     * 型号
     */
    @Excel(name = "型号", width = 15)
    private String model;
    /**
     * ERP现资产状态
     */
    @Excel(name = "ERP现资产状态", width = 15)
    private String assetStatus;
    /**
     * 退役时间
     */
    @Excel(name = "退役时间", width = 25, format = "yyyy/MM/dd")
    private Date retiredDate;
    /**
     * 是否有入库单
     */
    @Excel(name = "是否有入库单", width = 15)
    private String receiptIs;
    /**
     * 库存地点
     */
    @Excel(name = "库存地点", width = 15)
    private String theLocation;
    /**
     * 处置方式
     */
    @Excel(name = "处置方式", width = 15)
    private String disposalWay;
    /**
     * 新资产编号
     */
    @Excel(name = "新资产编号", width = 15)
    private String assetNo;
    /**
     * 项目分类
     */
    @Excel(name = "项目分类", width = 15)
    private String projectType;
    /**
     * 设备主人
     */
    @Excel(name = "设备主人", width = 15)
    private String equipmentOwners;
    /**
     * 地址
     */
    @Excel(name = "地址", width = 15)
    private String address;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private String note;
    /**
     * 设备号
     */
    @Excel(name = "设备号", width = 15)
    private String equipmentNo;
    /**
     * 实物已退役但未处置
     */
    @Excel(name = "实物已退役但未处置", width = 15)
    private String disposed;

    @Excel(name = "物料代码", width = 15)
    private String backup3;

    @Excel(name = "物料描述", width = 15)
    private String backup2;

    @Excel(name = "入库日期", width = 15, format = "yyyy/MM/dd")
    private String deliverTime;

    @Excel(name = "入库数量", width = 15)
    private String deliverNum;

    @Excel(name = "出库日期", width = 15, format = "yyyy/MM/dd")
    private String receivingTime;

    @Excel(name = "出库数量", width = 15)
    private String receivingNum;

    @Excel(name = "情况说明", width = 15)
    private String annotation;

    @Excel(name = "反馈日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date feedbackDateTime;
}
