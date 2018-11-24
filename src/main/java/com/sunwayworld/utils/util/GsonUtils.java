package com.sunwayworld.utils.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 99351 on 2018/3/29.
 */

public class GsonUtils {

    /**
     * 使用excludeFieldsWithoutExposeAnnotation创建的Gson对象时，
     * 没有@Expose注释的属性将不会被序列化.。另外想要不序列化某个属性，
     * 也可以使用transient。
     *
     * @return
     */
    public static Gson createGsonSpecial() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping()//跳过特殊符号转码
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return builder.create();
    }

    public static Gson createGson() {
        return new Gson();
    }

    public static JsonElement toJsonElement(String jsonString) throws Exception {
        if (TextUtils.isEmpty(jsonString)) {
            throw new Exception("数据源不能为null");
        }
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jsonString);
    }

    public static JsonArray toJsonArray(String jsonString) throws Exception {
        if (TextUtils.isEmpty(jsonString)) {
            throw new Exception("数据源不能为null");
        }
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jsonString).getAsJsonArray();
    }

    public static JsonArray toJsonArray(String key, String jsonString) throws Exception {
        if (TextUtils.isEmpty(jsonString)) {
            throw new Exception("数据源不能为null");
        }
        JsonParser jsonParser = new JsonParser();
        return toJsonArray(key, jsonParser.parse(jsonString).getAsJsonObject());
    }

    public static JsonArray toJsonArray(String key, JsonObject jsonObject) throws Exception {
        if (jsonObject == null) {
            throw new Exception("数据源不能为null");
        }
        if (jsonObject.has(key)) {
            return jsonObject.getAsJsonArray(key);
        } else {
            return new JsonArray();
        }
    }

    public static JsonObject toJsonObject(String jsonString) throws Exception {
        if (TextUtils.isEmpty(jsonString)) {
            throw new Exception("数据源不能为null");
        }
        return toJsonElement(jsonString).getAsJsonObject();
    }

    public static JsonObject toJsonObject(JsonElement jsonElement) throws Exception {
        if (jsonElement == null) {
            throw new Exception("数据源不能为null");
        }
        return jsonElement.getAsJsonObject();
    }

    public static JsonObject toJsonObject(String key, JsonObject jsonObject) throws Exception {
        if (jsonObject == null) {
            throw new Exception("数据源不能为null");
        }
        if (jsonObject.has(key)) {
            return jsonObject.getAsJsonObject(key);
        } else {
            return jsonObject;
        }
    }

    public static <T> String toJsonString(T object) throws Exception {
        return toJsonString(object, false);
    }

    public static <T> String toJsonString(T object, boolean isSpecial) throws Exception {
        if (object == null) {
            throw new Exception("数据源不能为null");
        }
        if (isSpecial) return createGsonSpecial().toJson(object);
        return createGson().toJson(object);
    }

    public static <T> JsonElement toJsonElement(T object, boolean isSpecial) throws Exception {
        if (object == null) {
            throw new Exception("数据源不能为null");
        }
        JsonParser jsonParser = new JsonParser();
        if (isSpecial) return jsonParser.parse(createGsonSpecial().toJson(object));
        return jsonParser.parse(createGson().toJson(object));
    }

    public static <T> JsonElement toJsonElement(T object) throws Exception {
        return toJsonElement(object, false);
    }

    public static <T> JsonObject addJson(String key, T object) throws Exception {
        return addJson(key, object, false);
    }

    public static <T> JsonObject addJson(String key, T object, boolean isSpecial) throws Exception {
        if (TextUtils.isEmpty(key)) {
            throw new Exception("key不能为null");
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(key, toJsonElement(object, isSpecial));
        return jsonObject;
    }

    public static <T> T formJson(JsonElement jsonElement, Class<T> classz) {
        return createGson().fromJson(jsonElement, classz);
    }

    public static <T> T formJson(String jsonString, Class<T> classz) {
        return createGson().fromJson(jsonString, classz);
    }

    public static <T> T formJson(String jsonString) {
        return createGson().fromJson(jsonString, new TypeToken<List<T>>() { }.getType());
    }

    public static <T> List<T> formJson(JsonArray jsonArray, Class<T> classz) {
        List<T> list = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            list.add(formJson(jsonElement, classz));
        }
        return list;
    }
}
