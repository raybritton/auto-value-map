package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class LongArrayMaker implements MapValueMaker<long[]> {
    @Override
    public long[] make(String methodName) {
        return new long[0];
    }
}
