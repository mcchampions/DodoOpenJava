package io.github.mcchampions.DodoOpenJava.Utils;

import com.alibaba.fastjson2.JSONObject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 一些有关于 Map 的实用方法
 */
public class MapUtil {
    /**
     * 遍历 Map
     * @param map 指定Map
     * @return 返回集合（集合中内置了一个集合，索引0是key，1是Value）
     */
    public static List<List<Object>> ergodicMaps(Map<?, ?> map) {
        Iterator<? extends Map.Entry<?, ?>> iter = map.entrySet().iterator();
        List<List<Object>> list = new ArrayList<>();
        while (iter.hasNext()) {
            List<Object> List = new ArrayList<>();
            List.add(iter.next().getKey());
            List.add(iter.next().getValue());
            list.add(List);
        }
        return list;
    }

    /**
     * map转bean
     *
     * @param type beanType
     * @param map  map
     * @return bean
     */
    public static <T> T mapToBean(Class<T> type, Map<?, ?> map) {
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            obj = type.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    descriptor.getWriteMethod().invoke(obj, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * map转json
     *
     * @param map  map
     * @return bean
     */
    public static JSONObject mapToJson(Map<?, ?> map) {
        return JSONObject.parseObject(map.toString());
    }
}
