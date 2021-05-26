package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.cable.entity.Plan2;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Plan2Vo
 * @Author Xm
 * @Date 2020/5/27 15:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Plan2Vo extends Plan2 implements Serializable {
    private static final long serialVersionUID = -5765834599690091801L;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBegin;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;
    /**
     * 仓库id
     */
    private Integer warehouseId;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 库位id
     */
    private Integer storageLocationId;
    /**
     * 库位名称
     */
    private String storageLocationName;
    /**
     * 可出库数量[批量完单操作时使用此属性]
     */
    private BigDecimal inventoryQuantity;
    /**
     * 批量完单的计划id集合
     */
    private String plan2Ids;
    /**
     * 完单类型[0:出库\1:入库]
     */
    private String operatorSchema;
    /**
     * 交接单号
     */
    private String plan2ReceiptNo;
    /**
     * 回单照片
     */
    private String receiptPhotos;
    /**
     * 任务日期
     */
    private String taskTime;
    /**
     * 合并完单填写的数据集
     */
    private List<?> completeOrderList;
}
