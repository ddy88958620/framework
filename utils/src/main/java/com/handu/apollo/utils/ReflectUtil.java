package com.handu.apollo.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by markerking on 14-4-14.
 */
public final class ReflectUtil {

    private ReflectUtil() {
    }

    // 返回所有命令的字段
    public static List<Field> getAllFieldsForClass(Class<?> cmdClass, Class<?> baseClass) {
        List<Field> fields = Lists.newArrayList();
        Collections.addAll(fields, cmdClass.getDeclaredFields());
        Class<?> superClass = cmdClass.getSuperclass();
        while (baseClass.isAssignableFrom(superClass) && baseClass != superClass) {
            Field[] superClassFields = superClass.getDeclaredFields();
            if (superClassFields != null) {
                Collections.addAll(fields, superClassFields);
            }
            superClass = superClass.getSuperclass();
        }
        return fields;
    }

    public static boolean isBaseType(Field field) {
        Class typeClass = field.getType();
        return typeClass.isPrimitive();
    }

    public static void setValueToDefault(Field field, Object obj) {
        if (!isBaseType(field)) {
            try {
                field.set(obj, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Type type = field.getType();
            try {
                if (type == long.class) {
                    field.setLong(obj, 0L);
                } else if (type == int.class) {
                    field.setInt(obj, 0);
                } else if (type == boolean.class) {
                    field.setBoolean(obj, false);
                } else if (type == float.class) {
                    field.setFloat(obj, 0.0F);
                } else if (type == double.class) {
                    field.setDouble(obj, 0.0D);
                } else if (type == short.class) {
                    field.setShort(obj, (short) 0);
                } else if (type == byte.class) {
                    field.setByte(obj, (byte) 0);
                } else if (type == char.class) {
                    field.setChar(obj, (char) 0);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
