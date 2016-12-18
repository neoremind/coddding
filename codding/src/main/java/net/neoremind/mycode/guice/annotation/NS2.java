package net.neoremind.mycode.guice.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xu.zhang on 12/18/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface NS2 {

}
