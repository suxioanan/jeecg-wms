package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName PlanVo
 * @Author Xm
 * @Date 2020/5/25 16:09
 */
@Data
public class PlanVo implements Serializable {
    private Integer id;
    /**
     * 操作模式
     */
    private Integer operatorSchema;
    /**
     * 目标仓库
     */
    private String warehouseName;

    /**目标仓库id*/
    private Integer warehouseId;

    /**目标仓库库位id*/
    private Integer storageLocationId;
    /**
     * 派单时间
     */
    private String sendOrdersTime;
    /**
     * 任务日期
     */
    private String accomplishTime;
    /**
     * 异常原因
     */
    private String anomalousCause;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 完单数量
     */
    private BigDecimal accomplishNum;
    /**
     * 单位
     */
    private Integer accomplishNumUnit;
    /**
     * 回单照片地址(多张)
     */
    private String receiptPhotoss;
    /**
     * 回单照片地址(第一张)
     */
    private String receiptPhotos;
    /**
     * 是否异常
     */
    private Integer sceneSituation;
    private String scenePhotos;
    private String scenePhotos1;
    private String scenePhotos2;
    /**
     * 说明
     */
    private String annotation;
    /**
     * 交接日期
     */
    private Date handoverDate;

    /**完单重量*/
    private java.math.BigDecimal accomplishWeight;

    /**完单重量完单重量（字典）*/
    private Integer accomplishWeightUnit;

    /**完单容积*/
    private java.math.BigDecimal accomplishVolume;

    /**回收规格*/
    private String recyclingSpecifications;

    /**材质（定死，铜，铝）*/
    private String texture;

    /**回收情况*/
    private String recyclingSituation;

    /**是否完成*/
    private String whetherComplete;

    /**入库交接单号*/
    private String receiptNo;

    /**未完成说明*/
    private String incompleteDescription;

    /**施工队*/
    private String constructionTeam;

    /**联系人*/
    private String linkman;

    /**电话*/
    private String phone;

    /** 可出库重量 */
    private String availableWeight;

    /** 资产编号 */
    private String assetNo;
}
