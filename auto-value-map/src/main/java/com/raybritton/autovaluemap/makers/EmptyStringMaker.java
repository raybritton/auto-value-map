package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class EmptyStringMaker implements MapValueMaker<String> {
    @Override
    public String make(String mapElementName) {
        return "";
    }
}
