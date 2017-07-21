package com.skymoe.light.http.annotation;

import com.skymoe.light.http.enums.RequestMethod;
import com.skymoe.light.http.enums.SerialType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义REST风格注解
 * 指定到方法和类那一级别，用法类似于RequestMapping
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Component //用于Spring扫描
public @interface Rest {
    /**
     * 请求路径
     *
     * @return
     */
    String path() default "";
    /**
     * Rest动作 即GET方法/POST方法 ,只对在方法上的注解奇效
     */
    RequestMethod[] method() default {};
    /**
     * 输出类型#默认为JSON类型,   只对在方法上的注解奇效
     */
   SerialType serial() default SerialType.JSON;
}
