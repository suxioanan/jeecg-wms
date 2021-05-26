package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;

/**
 * @ClassName SendOrdersTaskVo
 * @Author Xm
 * @Date 2020/5/22 11:34
 */
@Data
public class SendOrdersTaskVo implements Serializable {
    /**派单id*/
    private String id;
    /**计划id*/
    private String planId;
    /**计划表名*/
    private String planTypeName;
    /**车辆号码/员工id*/
    private String license;
    /**操作模式*/
    @Dict(dicCode = "operatorSchema")
    private String operatorSchema;
    /**仓库名称*/
    private String warehouseName;
    /**员工姓名*/
    private String realname;
    /**计划类型*/
    @Dict(dicCode = "plan1_type")
    private String planType;
    /**项目编码*/
    private String projectNo;
    /**项目名称*/
    private String projectName;
    /**项目地址*/
    private String engineeringAddress;

    /**
     * 车辆数量
     */
    private java.lang.Integer number;
}
