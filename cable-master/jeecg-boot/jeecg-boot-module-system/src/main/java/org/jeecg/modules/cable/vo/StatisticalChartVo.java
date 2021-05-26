package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *  统计视图Vo
 */

@Data
public class StatisticalChartVo implements Serializable {
    private String x;
    private String y;
    private String date;
}
