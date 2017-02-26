package com.raybritton.autovaluemap.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raybritton.autovaluemap.MapAdapter;

import java.lang.reflect.Type;
import java.util.List;

public class ListGson {
    private static final Gson gson = new GsonBuilder().create();

    public static class StringListAdapter implements MapAdapter<List<String>, String> {
        Type typeToken = new TypeToken<List<String>>(){}.getType();

        @Override
        public List<String> fromMap(String methodName, String str) {
            return gson.fromJson(str, typeToken);
        }

        @Override
        public String toMap(String methodName, List<String> list) {
            return gson.toJson(list, typeToken);
        }
    }

    public static class IntegerListAdapter implements MapAdapter<List<Integer>, String> {
        Type typeToken = new TypeToken<List<Integer>>(){}.getType();

        @Override
        public List<Integer> fromMap(String methodName, String str) {
            return gson.fromJson(str, typeToken);
        }

        @Override
        public String toMap(String methodName, List<Integer> list) {
            return gson.toJson(list, typeToken);
        }
    }

    public static class DoubleListAdapter implements MapAdapter<List<Double>, String> {
        Type typeToken = new TypeToken<List<Double>>(){}.getType();

        @Override
        public List<Double> fromMap(String methodName, String str) {
            return gson.fromJson(str, typeToken);
        }

        @Override
        public String toMap(String methodName, List<Double> list) {
            return gson.toJson(list, typeToken);
        }
    }

    public static class BooleanListAdapter implements MapAdapter<List<Boolean>, String> {
        Type typeToken = new TypeToken<List<Boolean>>(){}.getType();

        @Override
        public List<Boolean> fromMap(String methodName, String str) {
            return gson.fromJson(str, typeToken);
        }

        @Override
        public String toMap(String methodName, List<Boolean> list) {
            return gson.toJson(list, typeToken);
        }
    }

}
