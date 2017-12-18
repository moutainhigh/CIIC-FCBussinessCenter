package com.ciicsh.gto.salarymanagementcommandservice.aop;

/**
 * Created by jiangtianning on 2017/11/17.
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLogCutAnnotation {

    String value() default "";
}