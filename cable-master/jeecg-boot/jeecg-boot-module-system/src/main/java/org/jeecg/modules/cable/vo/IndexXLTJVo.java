package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 首页线路统计 Vo 类
 *
 * @author: 南独酌酒 <211425401@126.com>
 * @date: 2020/9/17 11:16
 */
@Data
@NoArgsConstructor
public class IndexXLTJVo implements Serializable {
    private static final long serialVersionUID = 7090526381658357783L;

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
     * 计划数量
     */
    private BigDecimal numReceipts;
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
