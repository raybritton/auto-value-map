package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class CharArrayMaker implements MapValueMaker<char[]> {
    @Override
    public char[] make(String methodName) {
        return new char[0];
    }
}
