package com.raybritton.autovaluemap;

/**
 * A MapValueMaker is used to provide values for methods not included in the map.
 *
 * @param <T> This is the type that the map will hold and this will create
 */
public interface MapValueMaker<T> {
    /**
     * @param methodName name of the method the instance is being made for
     * @return an instance of T to put in the object
     */
    T make(String methodName);
}
