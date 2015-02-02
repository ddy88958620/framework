package com.handu.apollo.utils;

import com.google.common.collect.Maps;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by markerking on 14-4-3.
 */
public final class MapUtil {

    public static boolean isNotEmpty(Map map) {
        return map != null && !map.isEmpty() && map.size() > 0;
    }

    private MapUtil() {}

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     * @param bean        要转化的JavaBean 对象
     * @return returnMap  转化出来的 Map 对象
     */
    public static <T> Map<String, Object> beanToMap(T bean) {

        Class<? extends Object> type = bean.getClass();
        Map<String, Object> returnMap = Maps.newHashMap();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    returnMap.put(propertyName, result);
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException("分析类属性失败", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("分析类属性失败", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("分析类属性失败", e);
        }
        return returnMap;
    }

    /**
     * 将一个Map对象转化为一个JavaBean
     *
     * @param paramMap  包含属性值的map
     * @param clazz 要转化的类型
     * @return beanObj    转化出来的JavaBean对象
     */
    public static <T> T mapToBean(Map<String, Object> paramMap, Class<T> clazz) {

        T beanObj = null;
        try {
            beanObj = clazz.newInstance();
            String propertyName = null;
            Object propertyValue = null;
            for (Map.Entry<String, Object> entity : paramMap.entrySet()) {
                propertyName = entity.getKey();
                propertyValue = entity.getValue();
                setProperties(beanObj, propertyName, propertyValue);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("不合法或不正确的参数", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("实例化JavaBean失败", e);
        } catch (Exception e) {
            throw new RuntimeException("Map转换为Java Bean对象失败", e);
        }
        return beanObj;
    }

    private static <T> void setProperties(T entity, String propertyName,
                                         Object value) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, entity.getClass());
        Method methodSet = pd.getWriteMethod();
        methodSet.invoke(entity, value);
    }
}
