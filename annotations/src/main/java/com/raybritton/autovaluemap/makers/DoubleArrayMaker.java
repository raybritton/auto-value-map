package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class DoubleArrayMaker implements MapValueMaker<double[]> {
    @Override
    public double[] make(String methodName) {
        return new double[0];
    }
}
