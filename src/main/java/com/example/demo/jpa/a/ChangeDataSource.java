package com.example.demo.jpa.a;

import java.lang.annotation.*;

/**
 * @author yang chen
 * @date 2019/4/17 11:07
 */


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChangeDataSource {
    String name() default "default";
}
