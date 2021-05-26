package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * WX小程序出入库处置VO
 * bai
 * 2020/6/15
 */
@Data
public class Deliver_Receiving_StorageVo implements Serializable {
    // TODO 派单ID
    private Integer sendOrderId;
    // TODO 物料ID
    private Integer materialId;
    // TODO 仓库ID
    private Integer warehouseId;
    // TODO 库位ID
    private Integer storageLocationId;
    // TODO 完单数量
    private BigDecimal accomplishNum;
    // TODO 完单单位
    private Integer unit;
    // TODO 完单状态
    private Integer state;
    // TODO 是否异常
    private Integer sceneSituation;
    // TODO 异常原因
    private String anomalousCause;
    // TODO 现场异常照片路径
    private String scenePhotos;
    // TODO 回单照片路径
    private String receiptPhotos;
    // TODO 入库时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverTime;
}
