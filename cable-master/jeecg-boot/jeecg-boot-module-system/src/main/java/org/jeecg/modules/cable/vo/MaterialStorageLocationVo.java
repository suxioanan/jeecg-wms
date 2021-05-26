package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MaterialStorageLocationVo implements Serializable {
    private Integer materialId;
    private BigDecimal amount;
    private BigDecimal accomplishVolume;
    private Integer storageLocationCId;
    private Integer storageLocationRId;
}
