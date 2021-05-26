package org.jeecg.modules.cable.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @ClassName inventoryIocationListVo(库存库位查询)
 * @Author Xm
 * @Date 2020/5/14 17:31
 */

@Data
public class InventoryIocationListVo implements Serializable {

  private String id;
  private String planType;
  /**单位(米，吨，卷，台，箱)*/
  @Excel(name = "单位(字典 米，吨，卷，台，箱)", width = 15)
  @ApiModelProperty(value = "单位(字典 米，吨，卷，台，箱)")
  @Dict(dicCode="unit")
  private Integer unit;
  /**仓库名称*/
  private String  warehouseName;
  private Integer warehouseId;
  /**项目编号*/
  private String  projectNo;
  /**项目名称*/
  private String  projectName;
  /**项目编号*/
  private String  serial;
  /**物料名称*/
  private String  materialName;
  /**当前库存*/
  private String  currentInventory;
}
