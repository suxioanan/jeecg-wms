package org.jeecg.modules.cable.dto;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Plan4Im implements Serializable {
    // 计划表4id
    @Excel(name = "序号", width = 15)
    private Integer id;

    // 工程名称
    @Excel(name = "工程名称", width = 15)
    private String engName;

    // 工程账号
    @Excel(name = "账号", width = 15)
    private String projectNo;

    // 电缆名称
    @Excel(name = "电缆名称", width = 15)
    private String cableName;

    // 电压等级
    @Excel(name = "电压等级", width = 15)
    private String voltageGrade;

    // 电缆截面
    @Excel(name = "电缆截面", width = 15)
    private String cableCross;

    // 预计抽取长度
    @Excel(name = "预计抽取长度", width = 15)
    private String samplingLength;

    // 抽取时期
    @Excel(name = "抽取时期", width = 25, format = "yyyy/MM/dd")
    private Date samplingDate;

    // 抽取地点
    @Excel(name = "抽取地点", width = 15)
    private String samplingAddres;

    // 施工班组
    @Excel(name = "施工班组", width = 15)
    private String construc;

    // 班组联系人及电话
    @Excel(name = "班组联系人及电话", width = 15)
    private String construcContact;

    // 施工队伍
    @Excel(name = "施工队伍", width = 15)
    private String team;

    // 队伍联系人及电话
    @Excel(name = "队伍联系人及电话", width = 15)
    private String teamContact;

    // 专职
    @Excel(name = "专职", width = 15)
    private String full;

    // 备注
    @Excel(name = "备注", width = 15)
    private String note;

    // 运检反馈
    @Excel(name = "运检反馈", width = 15)
    private String feedback;

    // 完单数量
    private BigDecimal accomplishNum;

    // 完单重量
    private BigDecimal accomplishWeight;

    // 实际回收数量/米
    @Excel(name = "实际回收数量/米", width = 15)
    private String actualRecoveredQuantity;

    // 实际回收规格
    @Excel(name = "实际回收规格", width = 15)
    private String actualRecyclingSpecifications;

    // 材质
    @Excel(name = "材质", width = 15)
    private String texture;

    // 回收情况
    @Excel(name = "回收情况", width = 15)
    private String recovery;

    // 回收日期
    @Excel(name = "回收日期", width = 25, format = "yyyy/MM/dd")
    private Date recoveryDate;

    // 负责人
    @Excel(name = "负责人", width = 15)
    private String fzpersion;

    // 是否完成
    @Excel(name = "是否完成", width = 15)
    private String completeState;

    // 交接单号
    @Excel(name = "交接单号", width = 15)
    private String itemSlip;

    // 备注/未完成情况说明
    @Excel(name = "备注/未完成情况说明", width = 15)
    private String instructions;

    // 库存点
    @Excel(name = "库存点", width = 15)
    private String Inventory;

    // 入终点仓时间
    @Excel(name = "入终点仓时间", width = 25, format = "yyyy/MM/dd")
    private Date receivingDateTime;

    // 入库交接单号
    @Excel(name = "入库交接单号", width = 15)
    private String receivingItemSlip;
}
