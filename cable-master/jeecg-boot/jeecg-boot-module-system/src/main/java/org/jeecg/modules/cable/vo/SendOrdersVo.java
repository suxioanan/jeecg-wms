package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.cable.entity.SendOrders;
import org.jeecg.modules.cable.entity.SendOrdersSubtabulation;
import org.jeecg.modules.cable.entity.Vehicle;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SendOrdersVo
 * @Author Xm
 * @Date 2020/5/26 15:07
 */
@Data
public class SendOrdersVo extends SendOrders implements Serializable {
    private static final long serialVersionUID = 1934731618945857271L;
    /**
     * liu
     * 2020-9-4
     */
    private String wasteMaterialText;//物料描述
    private BigDecimal inventoryQuantity;//可出库数量
    private String warehouseName;//仓库名称
    private String endWarehouseName;//终点仓库名称

    /**
     * zhu
     * 2020-08-28
     */
    private String wname;
    private String storagename;
    private String tasktime;
    @Dict(dicCode = "unit")
    private String accomplishnumunit;
    private BigDecimal accomplishVolume;
//    private String accomplishnum;
    private BigDecimal accomplishNum;
    private BigDecimal accomplishWeight;    //TODO accomplishWeight 字母含大写
    @Dict(dicCode = "unit")
    private String accomplishweightunit;
    private String phontos;//异常图片（多张）
    private String scenePhotos1;//第一张异常图片
    private String scenePhotos2;//第二张异常图片
    private String receiptPhotos;//回单照片
    private String receiptPhotoss;
    private String anomalousCause;
//    private String accomplishweight;    //TODO accomplishweight 字母纯小写
    private String mubiaock;// 目标仓库

    /**
     * zhu
     * 2020-09-11
     */
    private String annotation;//说明
    private String receiptNo;//交接单号
    private Integer zjid;// 自家仓库ID
    private Integer kwid;// 库位ID
    private Integer mbid;// 目标仓库ID
//    private String scenesituation;
    private String sceneSituation;//是否异常
    private Integer unit;// 完单数量单位
    private String texture;// 材质
    private String ptype;// 区分计划表
    private String zlchange;// 修改重量变化的值
    private String rjchange;// 修改容积变化的值
    private String slchange;// 修改容积变化的值
    private List<SendOrdersVo> completeOrderList; //完单信息集合


    /**
     * 计划id的集合
     */
    private String ids;

    /**
     * 物料编码
     */
    private String serial;

    /**
     * 车牌号集合
     */
    private List<String> license;

    /**
     * 车牌号
     */
    private String a0;

    /**
     * 人员 id
     */
    private String a1;

    /**
     * 库位
     */
    private String storageLocationName;

    /**
     * 任务详情
     */
    private String projectName;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 计划类型
     */
    private String pPlanType;

    /**
     * 物料描述
     * liu
     * 2020/07/15
     */
    private String rawMaterialText;

    /**
     * 交接单数量
     * liu
     * 2020/07/15
     */
    private String numReceipts;

    /**
     * 资产编号
     * liu
     * 2020/08/21
     */
    private String assetNo;

    /**
     * 派单信息集合
     */
    private List<SendOrdersVo> jeecgOrderCustomerList;

    /**
     * 派单车辆集合
     */
    private List<SendOrdersTaskVo> jeecgOrderTicketList;

    /**
     * 人员 id 集合
     */
    private List<String> realname;
}
