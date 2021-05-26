package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    public List<SysDictItem> selectItemsByMainId(String mainId);

    /**
     *  分页条件查询部门
     *
     * @param name
     * @return
     */
    IPage<SysDictItem> selectBYDict(String name, Page<SysDictItem> page);

    /**
     *   根据字典编号查询对应配置
     *
     * @param
     * @Author Xm
     * @Date 2020/5/19 9:23
     */
    List<SysDictItem> selectType(String type);

    Long getBumenId(String depart_name_id);


}
