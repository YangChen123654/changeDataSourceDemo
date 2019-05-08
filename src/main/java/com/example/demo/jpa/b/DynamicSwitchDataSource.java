package com.example.demo.jpa.b;

import java.lang.annotation.*;

/**
 * @author yang chen
 * @date 2019/4/18 11:04
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicSwitchDataSource {

    String dataSource() default "";
}
