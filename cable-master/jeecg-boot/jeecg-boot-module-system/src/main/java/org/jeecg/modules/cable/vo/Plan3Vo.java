package org.jeecg.modules.cable.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.cable.entity.Plan3;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: bai
 * @DateTime: 2020/8/28 13:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Plan3Vo extends Plan3 implements Serializable {
    private static final long serialVersionUID = 2926201890933829553L;
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
    private String plan3Ids;
    /**
     * 完单类型[0:出库\1:入库]
     */
    private String operatorSchema;
    /**
     * 交接单号
     */
    private String plan3ReceiptNo;
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
    private String warehousingNum;
    /**
     * 出库数量
     */
    private String warehouseOutNum;
    /**
     * 单位
     */
    private String itemText;
}


