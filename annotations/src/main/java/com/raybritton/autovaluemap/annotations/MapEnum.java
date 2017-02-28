package com.raybritton.autovaluemap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an automatic adapter for enums, it converts to and from their name
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface MapEnum {
}
