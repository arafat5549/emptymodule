package com.skymoe.light.http.annotation;

import java.lang.annotation.*;

/**
 * 路径解析  这样可利用url路径传参数
 *
 * 比如 /{id}
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVariable {
    String value() default "";
}
