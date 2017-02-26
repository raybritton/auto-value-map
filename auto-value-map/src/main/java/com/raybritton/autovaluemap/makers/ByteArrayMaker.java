package com.raybritton.autovaluemap.makers;

import com.raybritton.autovaluemap.MapValueMaker;

public class ByteArrayMaker implements MapValueMaker<byte[]> {
    @Override
    public byte[] make(String methodName) {
        return new byte[0];
    }
}
