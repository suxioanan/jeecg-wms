package org.jeecg.modules.cable.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;

/**
 * @ClassName InventoryListsVo
 * @Author Xm
 * @Date 2020/5/15 10:46
 */
@Data
public class InventoryListsVo implements Serializable {
    private String id;
    /**
     * 项目编码
     */
    private String projectNo;

    /**
     * 计划类型
     */
    private String planType;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 物料编号(唯一)
     */
    private String serial;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 库存数
     */
    private Integer quantityInStock;

    /**
     * 单位
     */
    @Dict(dicCode = "unit")
    private String unit;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 物料id
     */
    private String mId;

    /**
     * 资产编号
     */
    private String assetNo;
    /**
     * 采购订单号
     */
    private String proTheorderNo;
    /**
     * 库位id
     */
    private Integer storageLocationId;
}
