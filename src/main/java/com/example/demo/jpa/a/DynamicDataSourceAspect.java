package com.example.demo.jpa.a;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yang chen
 * @date 2019/4/17 11:09
 */
@Aspect
@Component
@Order(1)
public class DynamicDataSourceAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Resource
    private DataSourceProperties dataSourceProperties;

    @Resource
    private DynamicDataSource dynamicDataSource;

    @Pointcut("@within(com.example.demo.jpa.a.ChangeDataSource)||@annotation(com.example.demo.jpa.a.ChangeDataSource)")
    public void pointcut() {}

    @Before("pointcut()")
    public void changeDataSource(JoinPoint joinPoint) {
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

        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(dataSource);




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

    }

    @AfterReturning("pointcut()")
    public void restoreDataSource(JoinPoint point) {
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        DynamicDataSourceContextHolder.clearDataSourceType();
        LOGGER.info("clear change dataSource");
    }


}
