package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class NullMaker implements MapValueMaker {
    @Override
    public Object make(String mapElementName) {
        return null;
    }
}
