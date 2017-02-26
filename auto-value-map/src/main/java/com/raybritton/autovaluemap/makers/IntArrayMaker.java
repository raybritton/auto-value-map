package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class IntArrayMaker implements MapValueMaker<int[]> {
    @Override
    public int[] make(String methodName) {
        return new int[0];
    }
}
