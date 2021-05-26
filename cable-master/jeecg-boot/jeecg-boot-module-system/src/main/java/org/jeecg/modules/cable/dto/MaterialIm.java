package org.jeecg.modules.cable.dto;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物料信息导入 excel 工具表
 */
@Data
public class MaterialIm implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 物料编号
     */
    @Excel(name = "物料编号", width = 15)
    private String serial;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    private String name;

    /**
     * 规格型号
     */
    @Excel(name = "规格型号", width = 15)
    private String ations;

    /**
     * 供应商
     */
    @Excel(name = "供应商", width = 15)
    private String supplier;

    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    private String unit;

    /**
     * 容积
     */
    @Excel(name = "容积", width = 15)
    private BigDecimal materialVolume;
}
