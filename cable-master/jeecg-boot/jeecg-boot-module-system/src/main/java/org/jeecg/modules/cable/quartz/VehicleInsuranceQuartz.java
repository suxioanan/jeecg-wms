package org.jeecg.modules.cable.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.cable.entity.Insurance;
import org.jeecg.modules.cable.entity.Warehouse;
import org.jeecg.modules.cable.service.IInsuranceService;
import org.jeecg.modules.cable.service.IStorageLocationService;
import org.jeecg.modules.cable.service.IWarehouseService;
import org.jeecg.modules.cable.vo.StorageLocationVo;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 物料容积 and 车辆保险—定时任务
 */
@Component
public class VehicleInsuranceQuartz implements Job {
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private IInsuranceService insuranceService;
    @Autowired
    private IStorageLocationService storageLocationService;
    @Autowired
    private IWarehouseService warehouseService;

    // 交强险日期函数
    private final Calendar cale_strongDateEnd = Calendar.getInstance();
    // 商业险日期函数
    private final Calendar cale_insuranceDateEnd = Calendar.getInstance();
    // 当前日期
    private final Date nowDate = new Date();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        List<Insurance> insuranceList = insuranceService.list();
        for (Insurance insurance : insuranceList) {
            if (null != insurance.getStrongDateEnd()) {
                // 计算交强险结束日期两周前的时间
                cale_strongDateEnd.setTime(insurance.getStrongDateEnd());
                cale_strongDateEnd.add(Calendar.DATE, -14);
                // 得到交强险两周前的日期
                Date twoWeekTime = cale_strongDateEnd.getTime();
                // 判断如果两周前的数据同当前时间一致则提示用户车辆交强险到期
                // 当前日期小于交强险截止日期 && 当前日期大于提醒日期
                if (nowDate.before(insurance.getStrongDateEnd()) && nowDate.after(twoWeekTime)) {
                    addMsg("车辆交强险", "车牌号为：" + insurance.getLicense() + " 车辆交强险即将过期");
                }
            }
            if (null != insurance.getInsuranceDateEnd()) {
                // 计算商业险结束日期两周前的时间
                cale_insuranceDateEnd.setTime(insurance.getInsuranceDateEnd());
                cale_insuranceDateEnd.add(Calendar.DATE, -14);
                // 得到商业险两周前的日期
                Date twoWeekTime = cale_insuranceDateEnd.getTime();
                // 判断如果两周前的数据同当前时间一致则提示用户车辆商业险到期
                // 当前日期小于商业险截止日期 && 当前日期大于提醒日期
                if (nowDate.before(insurance.getInsuranceDateEnd()) && nowDate.after(twoWeekTime)) {
                    addMsg("车辆商业险", "车牌号为：" + insurance.getLicense() + " 车辆商业险将要到期");
                }
            }
        }
        List<StorageLocationVo> storageLocationList = storageLocationService.getAll(new QueryWrapper<>());
        for (StorageLocationVo storageLocationVo : storageLocationList) {
            // 判断库位容积是否达到 80%
            if (storageLocationVo.getPercentage() >= 80) {
                Warehouse byId = warehouseService.getById(storageLocationVo.getWarehouseId());
                addMsg("库位容积", byId.getName() + "下" + storageLocationVo.getStorageLocationName() + " 库位容积已达到80%");
            }
        }
    }

    /**
     * 发送消息到系统中
     *
     * @param title 消息标题
     * @param text  消息内容
     */
    public void addMsg(String title, String text) {
        SysAnnouncement sysAnnouncement = new SysAnnouncement();
        sysAnnouncement.setTitile(title);
        sysAnnouncement.setMsgContent(text);
        sysAnnouncement.setMsgCategory("1");
        sysAnnouncement.setMsgType("ALL");
        sysAnnouncement.setSendTime(new Date());
        sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
        sysAnnouncementService.save(sysAnnouncement);

        SysAnnouncementSend sysAnnouncementSend = new SysAnnouncementSend();
        sysAnnouncementSend.setAnntId(sysAnnouncement.getId());
        String userId = "e9ca23d68d884d4ebb19d07889727dae";
        sysAnnouncementSend.setUserId(userId);
        sysAnnouncementSend.setReadFlag(CommonConstant.DEL_FLAG_0.toString());
        sysAnnouncementSendService.save(sysAnnouncementSend);
    }
}
