package com.raybritton.autovaluemap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * By default the key for a value in the map is the method name, this annotation
 * can be used to replace the name with a custom one.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MapElementName {
    String value() default "";
}
