package com.skymoe.light.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * REST风格
 * 指定到方法和类那一级别，用法类似于RequestMapping
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Rest {
    /**
     * 请求路径
     *
     * @return
     */
    String path() default "";

}
