package com.study.reggie.common;

/**
 * @author That's all
 * 使用ThreadLocal操作同一个线程的数据
 */
public class BaseContext {
    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取Long类型的id
     *
     * @return
     */
    public static Long getCurrentId() {
        return (Long) THREAD_LOCAL.get();
    }

    /**
     * 线程中存入Long类型的id
     *
     * @param id
     */
    public static void setCurrentId(Long id) {
        THREAD_LOCAL.set(id);
    }

    /**
     * 获取String类型的
     *
     * @return
     */
    public static String getString() {
        return (String) THREAD_LOCAL.get();
    }

    /**
     * 保存String类型
     *
     * @param name
     */
    public static void setString(String name) {
        THREAD_LOCAL.set(name);
    }
}
