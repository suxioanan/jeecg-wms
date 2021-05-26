package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Plan4ExcelVo implements Serializable {
    private static final long serialVersionUID = 1931889797509480354L;


    // 开始时间
//    @Excel(name = "开始时间", width = 15)
    private String beginTime;

    // 截止时间
//    @Excel(name = "截止时间", width = 15)
    private String endTime;
    // 规格
    @Excel(name = "规格", width = 30)
    private String voltageGrade;
    // 材质
    @Excel(name = "材质", width = 15)
    private String texture;

    // 长度(M)
    @Excel(name = "入库长度(M)", width = 15)
    private String length;

    // 重量(吨)
    @Excel(name = "入库重量(吨)", width = 15)
    private String weight;

    // 出库重量（吨）神州仓库
    @Excel(name = "出库重量（吨）神州仓库", width = 15)
    private String retrievalWeight;

    // 反馈日期
    @Excel(name = "反馈日期", width = 15)
    private String decommissioningDate;

    // 来源
    @Excel(name = "来源", width = 30)
    private String engName;

    // 备注
    @Excel(name = "备注", width = 15)
    private String remark;
}
