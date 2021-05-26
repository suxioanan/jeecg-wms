package org.jeecg.modules.cable.vo;

import lombok.Data;

/**
 * 出车统计VO
 * bai
 * 2020/5/27
 */
@Data
public class DepartureStatisticsVo {
    /**
     * 车牌号码
     */
    private String license;

    /**
     * 1月出行次数
     */
    private int m1;

    /**
     * 2月出行次数
     */
    private int m2;

    /**
     * 3月出行次数
     */
    private int m3;

    /**
     * 4月出行次数
     */
    private int m4;

    /**
     * 5月出行次数
     */
    private int m5;

    /**
     * 6月出行次数
     */
    private int m6;

    /**
     * 7月出行次数
     */
    private int m7;

    /**
     * 8月出行次数
     */
    private int m8;

    /**
     * 9月出行次数
     */
    private int m9;

    /**
     * 10月出行次数
     */
    private int m10;

    /**
     * 11月出行次数
     */
    private int m11;

    /**
     * 12月出行次数
     */
    private int m12;

    /**
     * 一年出行总次数
     */
    private int nums;
}
