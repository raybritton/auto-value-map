package com.raybritton.autovaluemap.annotations;

import com.raybritton.autovaluemap.MapValueMaker;
import com.raybritton.autovaluemap.makers.NullMaker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If this annotation is set on a method then neither the key or value are put
 * into the map and when reading from the map,
 * if readFromMap is false (the default) or map doesn't have a value for this field
 * it's contents are ignored for this field and the value is used instead, by default
 * this is null but if it's true and map has a value for this field it will be read
 *
 *
 * The value must implement {@link MapValueMaker}
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface MapHide {
    Class value() default NullMaker.class;
    boolean readFromMap() default false;
}
