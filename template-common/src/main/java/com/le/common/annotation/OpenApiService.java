package com.le.common.annotation;

import java.lang.annotation.*;

/**
 * 开放接口实现类注解
 *
 * @author Bruce Lu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiService {
}
