package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class ShortArrayMaker implements MapValueMaker<short[]> {
    @Override
    public short[] make(String methodName) {
        return new short[0];
    }
}
