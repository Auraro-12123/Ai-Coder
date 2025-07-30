package com.hxc.aicode.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthCheck {

    /**
     * 必须有某个角色
     * @return
     */
    String mustRole() default "";
}
