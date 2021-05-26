package org.jeecg.common.constant;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;

public interface SysUserConstant {
    /**
     * 当前登录用户
     */
    public static final LoginUser SYS_USER = (LoginUser) SecurityUtils.getSubject().getPrincipal();
}
