package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 首页备品统计 Vo 类
 *
 * @author: 南独酌酒 <211425401@126.com>
 * @date: 2020/9/16 14:37
 */
@Data
@NoArgsConstructor
public class IndexBPTJVo implements Serializable {
    private static final long serialVersionUID = -4440363405074811180L;

    /**
     * 计划 2 id
     */
    private Integer id;
    /**
     * 物料简称
     */
    private String backup1;
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
     * 入库数量
     */
    private BigDecimal deliverNum;
    /**
     * 入库日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverTime;
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
