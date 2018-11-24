package com.sunwayworld.utils.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <pre>
 *     author: Yanghan
 *     time  : 2017/10/25
 *     desc  : 反射相关工具类
 * </pre>
 */
public class ReflectUtils {

    private ReflectUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 通过反射获取对象里key对应的set方法，用于存值
     *
     * @param bean  实体对象
     * @param key   属性名
     * @param value 保存值
     */
    public static <T> void setReflect(T bean, String key, Object value) {
        if (bean == null) {
            return;
        }
        Class cls = bean.getClass();
        try {
            Field f = cls.getDeclaredField(key);
            f.setAccessible(true);// 设置属性是可以访问的
            f.set(bean, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获取对象里key对应的get方法，用于取值
     *
     * @param bean 实体对象
     * @param key  属性名
     * @return 返回值
     */
    public static <T> Object getReflect(T bean, String key) {
        Object object = null;
        if (bean == null) {
            return null;
        }
        Class cls = bean.getClass();
        try {
            Field f = cls.getDeclaredField(key);
            f.setAccessible(true);// 设置属性是可以访问的
            object = f.get(bean);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 通过反射获取方法名对应的方法，用于存值
     *
     * @param bean          实体对象
     * @param methodName    方法名
     * @param value         需要存的值
     * @param parameterType 该值的类型，与value相互对应 例如：String.class、Integer.class、Boolean.class等
     */
    public static <T> void setReflectName(T bean, String methodName, Object value, Class parameterType) {
        if (bean == null) {
            return;
        }
        Class cls = bean.getClass();
        try {
            Method method = cls.getDeclaredMethod(methodName, parameterType);
            method.setAccessible(true);
            method.invoke(bean, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获取方法名对应的方法，用于取值
     *
     * @param bean       实体对象
     * @param methodName 方法名
     * @return 返回值
     */
    public static <T> Object getReflectName(T bean, String methodName) {
        Object object = null;
        if (bean == null) {
            return null;
        }
        Class cls = bean.getClass();
        try {
            Method method = cls.getDeclaredMethod(methodName);
            method.setAccessible(true);// 设置属性是可以访问的
            object = method.invoke(bean);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }
}
