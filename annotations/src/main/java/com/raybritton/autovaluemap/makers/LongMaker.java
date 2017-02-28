package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class LongMaker implements MapValueMaker<Long> {
    @Override
    public Long make(String methodName) {
        return 0L;
    }
}
