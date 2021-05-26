package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TaskVo
 * @Author Xm
 * @Date 2020/5/19 14:48
 */
@Data
public class TaskVo implements Serializable {
    /**日*/
    private String d;
    /**任务数*/
    private String cou;
}
