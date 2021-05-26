package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;

@Data
public class SettleAccountsVo implements Serializable {
    private String planId;
    private String planName;
    private String planType;
    private String projectNo;
    private String projectName;
    private String contact;
    private String phone;
    private String address;
    private String assetNo;
    @Dict(dicCode = "state")
    private String completeState;
    @Dict(dicCode = "settle_accounts_state")
    private String backup1;
}
