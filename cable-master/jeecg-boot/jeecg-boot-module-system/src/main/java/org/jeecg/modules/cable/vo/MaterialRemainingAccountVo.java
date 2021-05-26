package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物料余留台账Vo
 * bai
 * 2020/5/27
 */
@Data
public class MaterialRemainingAccountVo implements Serializable {
  private String planType;
  private String projectNo;
  private String projectName;
  private String serial;
  private String name;
  private BigDecimal now_5_1;
  private BigDecimal now_5_0;
  private BigDecimal now_4_1;
  private BigDecimal now_4_0;
  private BigDecimal now_3_1;
  private BigDecimal now_3_0;
  private BigDecimal now_2_1;
  private BigDecimal now_2_0;
  private BigDecimal now_1_1;
  private BigDecimal now_1_0;
  private BigDecimal now_0_1;
  private BigDecimal now_0_0;
  /**
   * 余留入库总数
   */
  private BigDecimal ylrkNums;
  /**
   * 余留出库总数
   */
  private BigDecimal ylckNums;
}
