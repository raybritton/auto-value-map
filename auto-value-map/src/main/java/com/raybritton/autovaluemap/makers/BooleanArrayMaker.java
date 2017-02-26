package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class BooleanArrayMaker implements MapValueMaker<boolean[]> {
    @Override
    public boolean[] make(String methodName) {
        return new boolean[0];
    }
}
