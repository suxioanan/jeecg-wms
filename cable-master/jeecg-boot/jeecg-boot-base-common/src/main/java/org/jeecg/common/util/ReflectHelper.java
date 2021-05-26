package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * @author 张代浩
 * @desc 通过反射来动态调用get 和 set 方法
 */
@Slf4j
public class ReflectHelper {
    /**
     * 传过来的对象
     */
    private Object obj;
    /**
     * 存放get方法
     */
    private Hashtable<String, Method> getMethods = null;
    /**
     * 存放set方法
     */
    private Hashtable<String, Method> setMethods = null;

    /**
     * 定义构造方法 -- 一般来说是个pojo
     *
     * @param o 目标对象
     */
    public ReflectHelper(Object o) {
        obj = o;
        initMethods();
    }

    /**
     * @desc 初始化
     */
    public void initMethods() {
        getMethods = new Hashtable<String, Method>();
        setMethods = new Hashtable<String, Method>();
        Class<?> cls = obj.getClass();
        Method[] methods = cls.getMethods();
        // 定义正则表达式，从方法中过滤出getter / setter 函数.
        String gs = "get(\\w+)";
        Pattern getM = Pattern.compile(gs);
        String ss = "set(\\w+)";
        Pattern setM = Pattern.compile(ss);
        // 把方法中的"set" 或者 "get" 去掉
        String rapl = "$1";
        String param;
        for (Method m : methods) {
            String methodName = m.getName();
            if (Pattern.matches(gs, methodName)) {
                param = getM.matcher(methodName).replaceAll(rapl).toLowerCase();
                getMethods.put(param, m);
            } else if (Pattern.matches(ss, methodName)) {
                param = setM.matcher(methodName).replaceAll(rapl).toLowerCase();
                setMethods.put(param, m);
            }
        }
    }

    /**
     * @desc 调用set方法
     */
    public void setMethodValue(String property, Object object) {
        Method m = setMethods.get(property.toLowerCase());
        if (m != null) {
            try {
                // 调用目标类的setter函数
                m.invoke(obj, object);
            } catch (Exception ex) {
                log.info("invoke getter on " + property + " error: " + ex.toString());
            }
        }
    }

    /**
     * @desc 调用set方法
     */
    public Object getMethodValue(String property) {
        Object value = null;
        Method m = getMethods.get(property.toLowerCase());
        if (m != null) {
            try {
                /*
                 * 调用obj类的setter函数
                 */
                value = m.invoke(obj);
            } catch (Exception ex) {
                log.info("invoke getter on " + property + " error: " + ex.toString());
            }
        }
        return value;
    }

    /**
     * 把map中的内容全部注入到obj中
     *
     * @param data
     */
    public void setAll(Map<String, Object> data) {
        if (data == null || data.keySet().size() <= 0) {
            return;
        }
        for (Entry<String, Object> entry : data.entrySet()) {
            this.setMethodValue(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 把map中的内容全部注入到obj中
     *
     * @param o
     * @param data
     * @return
     */
    public static Object setAll(Object o, Map<String, Object> data) {
        ReflectHelper reflectHelper = new ReflectHelper(o);
        reflectHelper.setAll(data);
        return o;
    }

    /**
     * 把map中的内容全部注入到新实例中
     *
     * @param clazz
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T setAll(Class<T> clazz, Map<String, Object> data) {
        T o = null;
        try {
            o = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            o = null;
            return o;
        }
        return (T) setAll(o, data);
    }

    /**
     * 根据传入的class将mapList转换为实体类list
     *
     * @param mapist
     * @param clazz
     * @return
     */
    public static <T> List<T> transList2Entrys(List<Map<String, Object>> mapist, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        if (mapist != null && mapist.size() > 0) {
            for (Map<String, Object> data : mapist) {
                list.add(ReflectHelper.setAll(clazz, data));
            }
        }
        return list;
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取属性名数组
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    public static List<Map<String, Object>> getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> infoMap = null;
        for (Field field : fields) {
            infoMap = new HashMap<String, Object>();
            infoMap.put("type", field.getType().toString());
            infoMap.put("name", field.getName());
            infoMap.put("value", getFieldValueByName(field.getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     */
    public static Object[] getFiledValues(Object o) {
        String[] fieldNames = getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            value[i] = getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }

}
