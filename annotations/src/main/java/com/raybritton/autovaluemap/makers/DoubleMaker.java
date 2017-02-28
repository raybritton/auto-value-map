package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class DoubleMaker implements MapValueMaker<Double> {
    @Override
    public Double make(String methodName) {
        return 0.0;
    }
}
