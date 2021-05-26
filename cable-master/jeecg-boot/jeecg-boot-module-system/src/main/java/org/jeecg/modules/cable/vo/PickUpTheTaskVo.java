package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * WX小程序接任务Vo
 * bai
 * 2020/6/11
 */
@Data
public class PickUpTheTaskVo implements Serializable {
    // TODO 主键id
    private Integer id;
    // TODO  派单id
    private Integer sendOrderId;
    // TODO  派单模式 [出库/入库]
    @Dict(dicCode = "operatorSchema")
    private Integer operatorSchema;
    // TODO  计划类型
    private String planType;
    // TODO  项目名称
    private String projectName;
    // TODO  计划任务操作
    private String disposalWay;
    // TODO  车牌号
    private String license;
    // TODO  地址
    private String address;
    // TODO  仓库地址
    private String theLocationText;
    // TODO  入库时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverTime;
    // TODO  完成状态
    @Dict(dicCode = "completeState")
    private Integer completeState;
    // TODO  物料ID
    private Integer materialId;
    // TODO  物料编号
    private String materialSerial;
    // TODO  物料名称
    private String materialName;
    // TODO  物料单位
    @Dict(dicCode = "unit")
    private Integer materialUnit;
    // TODO  仓库ID
    private Integer warehouseId;
    // TODO  仓库名称
    private String wareName;
    // TODO  库位ID
    private Integer storageLocationId;
    // TODO  库位名称
    private String storageLocationName;
}
