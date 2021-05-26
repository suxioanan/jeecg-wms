package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AnnualReportVo implements Serializable {
    private static final long serialVersionUID = 7032117115634107270L;

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
     * 1月出库合计
     */
    private BigDecimal m10;
    /**
     * 1月入库合计
     */
    private BigDecimal m11;
    /**
     * 1月剩余合计(入库-出库)
     */
    private BigDecimal remain1;
    /**
     * 2月出库合计
     */
    private BigDecimal m20;
    /**
     * 2月入库合计
     */
    private BigDecimal m21;
    /**
     * 2月剩余合计(入库-出库)
     */
    private BigDecimal remain2;
    /**
     * 3月出库合计
     */
    private BigDecimal m30;
    /**
     * 3月入库合计
     */
    private BigDecimal m31;
    /**
     * 3月剩余合计(入库-出库)
     */
    private BigDecimal remain3;
    /**
     * 4月出库合计
     */
    private BigDecimal m40;
    /**
     * 4月入库合计
     */
    private BigDecimal m41;
    /**
     * 4月剩余合计(入库-出库)
     */
    private BigDecimal remain4;
    /**
     * 5月出库合计
     */
    private BigDecimal m50;
    /**
     * 5月入库合计
     */
    private BigDecimal m51;
    /**
     * 5月剩余合计(入库-出库)
     */
    private BigDecimal remain5;
    /**
     * 6月出库合计
     */
    private BigDecimal m60;
    /**
     * 6月入库合计
     */
    private BigDecimal m61;
    /**
     * 6月剩余合计(入库-出库)
     */
    private BigDecimal remain6;
    /**
     * 7月出库合计
     */
    private BigDecimal m70;
    /**
     * 7月入库合计
     */
    private BigDecimal m71;
    /**
     * 7月剩余合计(入库-出库)
     */
    private BigDecimal remain7;
    /**
     * 8月出库合计
     */
    private BigDecimal m80;
    /**
     * 8月入库合计
     */
    private BigDecimal m81;
    /**
     * 8月剩余合计(入库-出库)
     */
    private BigDecimal remain8;
    /**
     * 9月出库合计
     */
    private BigDecimal m90;
    /**
     * 9月入库合计
     */
    private BigDecimal m91;
    /**
     * 9月剩余合计(入库-出库)
     */
    private BigDecimal remain9;
    /**
     * 10月出库合计
     */
    private BigDecimal m100;
    /**
     * 10月入库合计
     */
    private BigDecimal m101;
    /**
     * 10月剩余合计(入库-出库)
     */
    private BigDecimal remain10;
    /**
     * 11月出库合计
     */
    private BigDecimal m110;
    /**
     * 11月入库合计
     */
    private BigDecimal m111;
    /**
     * 11月剩余合计(入库-出库)
     */
    private BigDecimal remain11;
    /**
     * 12月出库合计
     */
    private BigDecimal m120;
    /**
     * 12月入库合计
     */
    private BigDecimal m121;
    /**
     * 12月剩余合计(入库-出库)
     */
    private BigDecimal remain12;
    /**
     * 全年出库合计
     */
    private BigDecimal mh0;
    /**
     * 全年入库合计
     */
    private BigDecimal mh1;
    /**
     * 全年剩余合计(入库-出库)
     */
    private BigDecimal mhRemain;
}
