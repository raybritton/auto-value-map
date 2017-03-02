package com.raybritton.autovaluemap.annotations;

import com.raybritton.autovaluemap.InvalidAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adapters are needed for any non primitive value in an autovalue class,
 * for enums use {@link MapEnum}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MapElementAdapter {
    Class adapter() default InvalidAdapter.class;
    Class mapType() default String.class;
}
