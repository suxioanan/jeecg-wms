package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 计划结算详情Vo
 * bai
 * 2020/6/10
 */
@Data
public class SettleAccountsDetailsVo implements Serializable {
    // id
    private Integer id;
    // 计划id
    private Integer planId;
    // 计划类型
    private String pPlanType;
    // 派单计划类型
    private String planType;
    // 项目名称
    private String projectName;
    // 联系人
    private String linkman;
    // 联系电话
    private String phone;
    // 地址
    private String address;
    // 任务时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date taskTime;
    // 任务车辆
    private String a0;
    // 任务人员
    private String a1;

    /**
     * 废旧物料名称
     * liu
     * 2020/7/15
     */
    private String wasteMaterialText;

    /**
     * 物料名称
     * liu
     * 2020/7/15
     */
    private String rawMaterialText;
    /**
     * 入库数量
     */
    private String receivingNum;
    /**
     * 出库数量
     */
    private String deliverNum;
    /**
     * 物料数量
     * liu
     * 2020/7/15
     */
    private String numReceipts;
    /**
     * 物料单位
     * liu
     * 2020/7/15
     */
    @Dict(dicCode = "unit")
    private String wasteMaterialUnit;
    /**
     * 采购订单号
     * liu
     * 2020/7/15
     */
    private String proTheorderNo;
}
