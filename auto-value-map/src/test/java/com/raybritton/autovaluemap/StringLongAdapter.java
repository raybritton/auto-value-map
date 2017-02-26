package com.raybritton.autovaluemap;

public class StringLongAdapter implements MapAdapter<Long, String> {
    @Override
    public Long fromMap(String methodName, String cal) {
        return Long.valueOf(cal);
    }

    @Override
    public String toMap(String methodName, Long time) {
        return "" + time;
    }
}
