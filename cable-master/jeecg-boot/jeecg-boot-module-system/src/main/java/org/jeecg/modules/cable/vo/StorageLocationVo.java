package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库位设置查询数据 VO
 * bai
 * 2020/6/8
 */
@Data
public class StorageLocationVo implements Serializable {
    // 主键ID
    private Integer id;
    // 仓库ID
    private Integer warehouseId;
    // 库位名称
    private String storageLocationName;
    // 库位容积
    private BigDecimal storageLocationVolume;
    // 当前容积
    private BigDecimal theCurrentVolume;
    // 库位所占百分比
    private Double percentage;
}
