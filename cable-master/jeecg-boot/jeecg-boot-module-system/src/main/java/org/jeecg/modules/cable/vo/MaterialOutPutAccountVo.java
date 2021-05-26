package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物料出入库台账 Vo
 * bai
 * 2020/6/9
 */
@Data
public class MaterialOutPutAccountVo implements Serializable {
    // 返回JSON所需参数
    // 主键id
    private Integer id;
    // 物料编号
    private String materialSerial;
    // 物料名称
    private String materialName;
    // 规格型号
    private String materialAtions;
    // 供应商
    private String materialSupplier;
    // 物料单位
    @Dict(dicCode = "unit")
    private Integer materialUnit;
    // 入库数量
    private BigDecimal deliverNum;
    // 出库数量
    private BigDecimal receivingNum;
    // 余留数量
    private BigDecimal ylNum;

    // 查询所需参数
    // 年份
    private String year;
    // 季度
    private String quarter;
    // 月份
    private String month;
    // 周
    private String week;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
}
