package org.jeecg.common.system.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.common.system.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20
 * @Version:V1.0
 */
public interface ISysBaseAPI {
    /**
     * 日志添加
     *
     * @param LogContent  内容
     * @param logType     日志类型(0:操作日志;1:登录日志;2:定时任务)
     * @param operateType 操作类型(1:添加;2:修改;3:删除;)
     */
    void addLog(String LogContent, Integer logType, Integer operateType);

    /**
     * 根据用户账号查询用户信息
     *
     * @param username 用户名
     * @return LoginUser.class
     */
    public LoginUser getUserByName(String username);

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户 id
     * @return LoginUser.class
     */
    public LoginUser getUserById(String id);

    /**
     * 通过用户账号查询角色集合
     *
     * @param username 用户名
     * @return List<String> 角色集合
     */
    public List<String> getRolesByUsername(String username);

    /**
     * 通过用户账号查询部门集合
     *
     * @param username 用户名
     * @return 部门集合id
     */
    List<String> getDepartIdsByUsername(String username);

    /**
     * 通过用户账号查询部门 name
     *
     * @param username 用户名
     * @return 部门集合名称
     */
    List<String> getDepartNamesByUsername(String username);

    /**
     * 获取当前数据库类型
     *
     * @return 数据库类型
     */
    public String getDatabaseType() throws SQLException;

    /**
     * 获取数据字典
     *
     * @param code 字典编码
     * @return 字典数据
     */
    public List<DictModel> queryDictItemsByCode(String code);

    /**
     * 查询所有的父级字典，按照create_time排序
     *
     * @return 字典数据
     */
    public List<DictModel> queryAllDict();

    /**
     * 查询所有分类字典
     *
     * @return 分类字典数据
     */
    public List<SysCategoryModel> queryAllDSysCategory();

    /**
     * 获取表数据字典
     *
     * @return 字典数据
     */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     *
     * @return 字典数据
     */
    public List<DictModel> queryAllDepartBackDictModel();

    /**
     * 查询所有部门，拼接查询条件
     *
     * @return 部门信息
     */
    List<JSONObject> queryAllDepart(Wrapper wrapper);

    /**
     * 发送系统消息
     *
     * @param fromUser   发送人(用户登录账户)
     * @param toUser     发送给(用户登录账户)
     * @param title      消息主题
     * @param msgContent 消息内容
     */
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent);

    /**
     * 发送系统消息
     *
     * @param fromUser     发送人(用户登录账户)
     * @param toUser       发送给(用户登录账户)
     * @param title        通知标题
     * @param map          模板参数
     * @param templateCode 模板编码
     */
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map, String templateCode);

    /**
     * @param fromUser     发送人(用户登录账户)
     * @param toUser       发送给(用户登录账户)
     * @param title        通知标题
     * @param map          模板参数
     * @param templateCode 模板编码
     * @param busType      业务类型
     * @param busId        业务id
     */
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map, String templateCode, String busType, String busId);

    /**
     * 通过消息中心模板，生成推送内容
     *
     * @param templateCode 模板编码
     * @param map          模板参数
     * @return 推送内容
     */
    public String parseTemplateByCode(String templateCode, Map<String, String> map);

    /**
     * 发送系统消息
     *
     * @param fromUser       发送人(用户登录账户)
     * @param toUser         发送给(用户登录账户)
     * @param title          消息主题
     * @param msgContent     消息内容
     * @param setMsgCategory 消息类型 1:消息2:系统消息
     */
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory);

    /**
     * queryTableDictByKeys
     * 发送系统消息
     *
     * @param fromUser       发送人(用户登录账户)
     * @param toUser         发送给(用户登录账户)
     * @param title          消息主题
     * @param msgContent     消息内容
     * @param setMsgCategory 消息类型 1:消息2:系统消息
     * @param busType        业务类型
     * @param busId          业务id
     */
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory, String busType, String busId);

    /**
     * 根据业务类型及业务id修改消息已读
     *
     * @param busType 业务类型
     * @param busId   业务id
     */
    public void updateSysAnnounReadFlag(String busType, String busId);

    /**
     * 查询表字典 支持过滤数据
     *
     * @return 字典数据
     */
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql);

    /**
     * 查询指定table的 text code 获取字典，包含text和value
     *
     * @return 字典数据
     */
    @Deprecated
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray);

    /**
     * 获取所有有效用户
     *
     * @return 有效用户
     */
    public List<ComboModel> queryAllUser();

    /**
     * 获取所有有效用户 带参
     * userIds 默认选中用户
     *
     * @return 有效用户
     */
    public JSONObject queryAllUser(String[] userIds, int pageNo, int pageSize);

    /**
     * 获取所有有效用户 拼接查询条件
     *
     * @return 有效用户
     */
    List<JSONObject> queryAllUser(Wrapper wrapper);

    /**
     * 获取所有角色
     *
     * @return 所有角色
     */
    public List<ComboModel> queryAllRole();

    /**
     * 获取所有角色 带参
     * roleIds 默认选中角色
     *
     * @return 所有角色
     */
    public List<ComboModel> queryAllRole(String[] roleIds);

    /**
     * 通过用户账号查询角色Id集合
     *
     * @param username 用户账号
     * @return 角色Id集合
     */
    public List<String> getRoleIdsByUsername(String username);

    /**
     * 通过部门编号查询部门id
     *
     * @param orgCode 部门编号
     * @return 部门id
     */
    public String getDepartIdsByOrgCode(String orgCode);

    /**
     * 查询上一级部门
     *
     * @param departId 部门id
     * @return 上级部门
     */
    public DictModel getParentDepartId(String departId);

    /**
     * 查询所有部门
     *
     * @return 所有部门
     */
    public List<SysDepartModel> getAllSysDepart();

    /**
     * 根据 id 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceId 数据库id
     * @return DynamicDataSourceModel
     */
    DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId);

    /**
     * 根据 code 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceCode code
     * @return DynamicDataSourceModel
     */
    DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode);

    /**
     * 根据部门Id获取部门负责人
     *
     * @param deptId 部门Id
     * @return 部门负责人
     */
    public List<String> getDeptHeadByDepId(String deptId);

    /**
     * 文件上传
     *
     * @param file       文件
     * @param bizPath    自定义路径
     * @param uploadType 上传方式
     * @return 上传成功的路径
     */
    public String upload(MultipartFile file, String bizPath, String uploadType);

    /**
     * 文件上传 自定义桶
     *
     * @param file         文件
     * @param bizPath      自定义路径
     * @param uploadType   上传方式
     * @param customBucket 自定义桶
     * @return 上传成功的路径
     */
    public String upload(MultipartFile file, String bizPath, String uploadType, String customBucket);

    /**
     * 文档管理文件下载预览
     *
     * @param filePath   文件路径
     * @param uploadPath 下载路径
     * @param response   http响应
     */
    public void viewAndDownload(String filePath, String uploadPath, String uploadType, HttpServletResponse response);


    /**
     * 给指定用户发消息
     *
     * @param userIds 用户 id 集合,可以发送多个用户
     * @param cmd     消息内容
     */
    public void sendWebSocketMsg(String[] userIds, String cmd);

    /**
     * 根据id获取所有参与用户
     *
     * @param userIds 用户 id 集合
     * @return 所有参与用户
     */
    public List<LoginUser> queryAllUserByIds(String[] userIds);

    /**
     * 将会议签到信息推动到预览
     *
     * @param userId 用户 id
     */
    void meetingSignWebsocket(String userId);

    /**
     * 根据name获取所有参与用户
     *
     * @param userNames 用户姓名
     * @return 所有参与用户
     */
    List<LoginUser> queryUserByNames(String[] userNames);
}
