package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class BooleanMaker implements MapValueMaker<Boolean> {
    @Override
    public Boolean make(String mapElementName) {
        return false;
    }
}
