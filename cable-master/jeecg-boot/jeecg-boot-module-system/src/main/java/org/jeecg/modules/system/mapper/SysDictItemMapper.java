package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysDictItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {
    @Select("SELECT * FROM sys_dict_item WHERE DICT_ID = #{mainId} order by sort_order asc, item_value asc")
    public List<SysDictItem> selectItemsByMainId(String mainId);

    /**
     *   根据字典编号查询对应配置
     *
     * @param
     * @Author Xm
     * @Date 2020/5/19 9:21
     */
    List<SysDictItem> selectType(@Param("type")String type);

    Long getBumenId(@Param("dictId")String depart_name_id);

    List<SysDictItem> selectBYDict(@Param("name")String name, @Param("page")Page<SysDictItem> page);
}
