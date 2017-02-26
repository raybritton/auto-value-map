package com.raybritton.autovaluemap;

/**
 * Used to convert between an object in the AutoValue model and element in the map
 * @param <M> The type of the method in the model
 * @param <O> The type of the object in the map
 */
public interface MapAdapter<M, O> {
    /**
     *
     * @param obj this is the map value
     * @param methodName name of the method being converted
     * @return A value to be put in the model
     */
    M fromMap(String methodName, O obj);
    /**
     *
     * @param obj this is the model value
     * @param methodName name of the method being converted
     * @return A value to be put in the map
     */
    O toMap(String methodName, M obj);
}
