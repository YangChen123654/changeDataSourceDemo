package com.example.demo.jpa.a;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang chen
 * @date 2019/4/17 11:01
 */
@Component
public class DynamicDataSourceContextHolder {
        /**
         * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
         * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
         */
        private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
        /**
         * 校验输入的数据库名称是否正确
         */
        private static List<String> dataSourceList=new ArrayList<String>();

        /**
         * 使用setDataSourceType设置当前的
         * @param dataSourceType
         */
        public static void setDataSourceType(String dataSourceType) {
            contextHolder.set(dataSourceType);
        }

        public static String getDataSourceType() {
            return contextHolder.get()==null?"default":contextHolder.get();
        }


        public static void clearDataSourceType() {
            contextHolder.remove();
        }

        public static void saveDataSourceTypeName(String name){
            dataSourceList.add(name);
        }

        public static boolean checkDataSourceType(String name){
            return dataSourceList.contains(name);
        }





}
