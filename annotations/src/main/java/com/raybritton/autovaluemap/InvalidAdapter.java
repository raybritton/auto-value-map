package com.raybritton.autovaluemap;

public class InvalidAdapter implements MapAdapter {
    @Override
    public Object fromMap(String methodName, Object obj) {
        throw new IllegalStateException("A MapAdapter must be set in every @MapElementAdapter (not set for " + methodName + ")");
    }

    @Override
    public Object toMap(String methodName, Object obj) {
        throw new IllegalStateException("A MapAdapter must be set in every @MapElementAdapter (not set for " + methodName + ")");
    }
}
