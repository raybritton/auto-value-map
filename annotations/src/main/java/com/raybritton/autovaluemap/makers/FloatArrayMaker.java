package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class FloatArrayMaker implements MapValueMaker<float[]> {
    @Override
    public float[] make(String methodName) {
        return new float[0];
    }
}
