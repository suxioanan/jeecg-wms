package org.jeecg.modules.cable.vo;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName StorageLocationListVo
 * @Author Xm
 * @Date 2020/5/14 17:31
 */

@Data
public class StorageLocationListVo implements Serializable {
    private Integer id;
    /**库位名称*/
    private String storageLocationName;
    /**物料编号*/
    private String materialId;

    private Integer sum;
    /**项目编号*/
    private  String projectNo;


}
