package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * ruan
 * ClassName: YikuVo <br/>
 * Description: <br/>
 * date: 2020/6/11 19:20<br/>
 *
 * @author Administrator<br />
 * @since JDK 1.8
 */
@Data
public class YikuVo implements Serializable {
    private Integer id;
    private String name1;
    private String name2;
    private String wcode;
    private String wuliaoName;
    private String yksum;
    @Dict(dicCode = "unit")
    private Integer unit;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
