package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.cable.entity.Plan1;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Plan1Vo
 * @Author Xm
 * @Date 2020/5/27 15:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Plan1Vo extends Plan1 implements Serializable {
    private static final long serialVersionUID = 6544151396980781818L;

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
     * 可出库数量[批量完单操作时使用此属性] ----2020/8/26 bai
     */
    private BigDecimal inventoryQuantity;
    /**
     * 批量完单的计划 id集合
     */
    private String plan1Ids;
    /**
     * 完单类型[0:出库\1:入库]
     */
    private String operatorSchema;
    /**
     * 交接单号
     */
    private String receiptNo;
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
    /**
     * 入库数量
     */
    private String stockNum;
    /**
     * 出库数量
     */
    private String receivingNum;
    /**
     * 单位
     */
    private String rawMaterial;

}
