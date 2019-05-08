package com.example.demo.jpa.b;


import com.example.demo.jpa.a.DynamicDataSourceContextHolder;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yang chen
 * @date 2019/4/18 11:06
 */
@Aspect
@Component
@Order(1)
public class HandlerDataSourceAop {
    @Resource
    private DataSourceProperties dataSourceProperties;
    //@within在类上设置
    //@annotation在方法上进行设置
    @Pointcut("@within(com.example.demo.jpa.b.DynamicSwitchDataSource)||@annotation(com.example.demo.jpa.b.DynamicSwitchDataSource)")
    public void pointcut() {}

    @Before("pointcut()")
    public void changeDataSource(JoinPoint joinPoint)
    {
//        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
//        DynamicSwitchDataSource annotationClass = method.getAnnotation(DynamicSwitchDataSource.class);//获取方法上的注解
//        if(annotationClass == null){
//            annotationClass = joinPoint.getTarget().getClass().getAnnotation(DynamicSwitchDataSource.class);//获取类上面的注解
//            if(annotationClass == null) return;
//        }
//        //获取注解上的数据源的值的信息
//        String dataSourceKey = annotationClass.dataSource();
//        if(dataSourceKey !=null){
//            //给当前的执行SQL的操作设置特殊的数据源的信息
//            HandlerDataSource.putDataSource(dataSourceKey);
//        }
//        System.out.println("AOP动态切换数据源，className"+joinPoint.getTarget().getClass().getName()+"methodName"+method.getName()+";dataSourceKey:"+dataSourceKey==""?"默认数据源":dataSourceKey);

        MultipleDataSourceToChoose ss = new MultipleDataSourceToChoose();
        String name= "";
        System.out.println("name :"+name);

        String dataSourceKey = "default";
        Map<Object, Object> targetDataSources=new HashMap<>();
        DataSource dataSource = new ComboPooledDataSource();
        if(null == name || "".equalsIgnoreCase(name)){
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceKey);
            targetDataSources.put(dataSourceKey,dataSource);
        }else{
            dataSourceKey = name;
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceKey);
            ((ComboPooledDataSource) dataSource).setJdbcUrl(dataSourceProperties.getUrl()+dataSourceKey);
            System.out.println(((ComboPooledDataSource) dataSource).getJdbcUrl());

            targetDataSources.put(dataSourceKey,dataSource);
        }

        ss.setTargetDataSources(targetDataSources);
        ss.setDefaultTargetDataSource(dataSource);


    }

    @After("pointcut()")
    public void after(JoinPoint point) {
        //清理掉当前设置的数据源，让默认的数据源不受影响
        HandlerDataSource.clear();
    }

}
