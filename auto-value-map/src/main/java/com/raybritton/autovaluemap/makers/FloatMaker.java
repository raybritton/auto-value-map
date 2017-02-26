package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class FloatMaker implements MapValueMaker<Float> {
    @Override
    public Float make(String mapElementName) {
        return 0f;
    }
}
