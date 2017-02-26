package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class IntegerMaker implements MapValueMaker<Integer> {
    @Override
    public Integer make(String mapElementName) {
        return 0;
    }
}
