package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 出入库台账 Vo 类
 * bai
 * 2020/9/1
 */
@Data
public class OutPutWarehouseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 操作类型[1:入库\0:出库]
     */
    @Dict(dicCode = "operatorSchema")
    private Integer operatorSchema;
    /**
     * 操作时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date taskTime;
    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    /**
     * 计划类型
     */
    private String planType;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 物料编号
     */
    private String serial;
    /**
     * 物料名称
     */
    private String name;
    /**
     * 资产编号
     */
    private String assetNo;
    /**
     * 采购订单号
     */
    private String proTheorderNo;
    /**
     * 操作数量
     */
    private BigDecimal accomplishNum;
    /**
     * 操作数量单位
     */
    @Dict(dicCode = "unit")
    private Integer accomplishNumUnit;
    /**
     * 操作数量拼接上操作单位返回给前端展示 2020/9/4
     */
    private String accomplishNumConcatUnit;
    /**
     * 操作重量
     */
    private BigDecimal accomplishWeight;
    /**
     * 操作重量拼接上操作单位返回给前端展示 2020/9/4
     */
    private String accomplishWeightConcatUnit;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 库位名称
     */
    private String storageLocationName;
    /**
     * 交接单号
     */
    private String receiptNo;
    /**
     * 终点仓库名称
     */
    private String endWarehouseName;
    /**
     * 情况说明
     */
    private String annotation;
}
