package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 首页临措统计 Vo 类
 *
 * @author: 南独酌酒 <211425401@126.com>
 * @date: 2020/9/17 14:56
 */
@Data
@NoArgsConstructor
public class IndexLCTJVo implements Serializable {
    private static final long serialVersionUID = 7109210413940256082L;

    /**
     * 物料简称
     */
    private String backup1;
    /**
     * 入库数量
     */
    private BigDecimal deliverNum;
    /**
     * 入库时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverTime;
    /**
     * 出库数量
     */
    private BigDecimal receivingNum;
    /**
     * 出库日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivingTime;
    /**
     * 开始日期[首页备品统计查询用]
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;
    /**
     * 结束日期[首页备品统计查询用]
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
