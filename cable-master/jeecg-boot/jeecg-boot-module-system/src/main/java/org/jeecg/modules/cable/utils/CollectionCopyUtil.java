package org.jeecg.modules.cable.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionCopyUtil {
    public static <T> List<?> copyList(List<T> list, Class<Object> tClass) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(JSON.toJSONString(list), tClass);
    }

    public static Map<String, Object> copyMap(Map<Object, Object> map) {
        return JSON.parseObject(JSON.toJSONString(map));
    }
}
