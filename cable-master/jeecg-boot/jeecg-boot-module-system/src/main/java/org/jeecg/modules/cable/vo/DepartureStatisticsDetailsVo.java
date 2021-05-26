package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 出车统计详情VO
 * bai
 * 2020/5/27
 */
@Data
public class DepartureStatisticsDetailsVo {
    /**
     * 任务日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任务日期")
    private Date taskTime;

    /**
     * 车牌号
     */
    private String license;

    /**
     * 任务类型 0.出库，1.入库
     */
    @Dict(dicCode = "send_orders_type")
    private Integer rwType;

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目地址
     */
    private String projectAddress;
}
