package org.javabeast.easy.http.util.json;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * This class is made for converting Objects into JSONObjects / JSONArrays, and the other way around.
 * @author Java3east
 * @version 1.0
 */
public class Json {
    private static @NotNull Object toJson(@NotNull Object object) {
        if (object instanceof List<?>) {
            JSONArray jsonArray = new JSONArray();
            for(Object o : ((List) object)) {
                jsonArray.add(toJson(o));
            }

            return jsonArray;
        } else if(object instanceof Object[]) {
            JSONArray jsonArray = new JSONArray();
            for(Object o : ((Object[]) object)) {
                jsonArray.add(toJson(o));
            }
            return jsonArray;
        } else if (object instanceof HashMap<?,?>) {
            return jsonMap((HashMap<?, ?>) object);
        }else {
            return object;
        }
    }

    private static JSONObject jsonMap(HashMap<?, ?> map) {
        JSONObject jsonObject = new JSONObject();

        for (Object key : map.keySet()) {
            Object value = map.get(key);
            jsonObject.put(toJson(key), toJson(value));
        }

        return jsonObject;
    }

    /**
     * Convert a JSONArray to an Object[]
     * @see List#toArray()
     * @param jsonArray the JSONArray to convert
     * @return the converted JSONArray in form of an Object[]
     */
    public static @NotNull Object[] toArray(JSONArray jsonArray) {
        return jsonArray.toArray();
    }

    /**
     * Convert a JSONObject to a HashMap
     * @param jsonObject the JSONObject to convert
     * @return the converted JSONObject in form of a HashMap
     */
    public static @NotNull HashMap<Object, Object> toHashMap(@NotNull JSONObject jsonObject) {
        HashMap<Object, Object> hashMap = new HashMap<>();

        for (Object key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (key instanceof JSONObject) {
                key = toHashMap((JSONObject) key);
            } else if (key instanceof JSONArray) {
                key = toArray((JSONArray) key);
            }

            if (value instanceof JSONObject) {
                value = toHashMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = toArray((JSONArray) value);
            }

            hashMap.put(key, value);
        }

        return hashMap;
    }

    /**
     * get a JSONArray from any Object
     * @param object the object to convert to JSONArray
     * @return the created JSONArray
     */
    public static @NotNull JSONArray toJSON(@NotNull Object object) {
        JSONArray jsonArray = new JSONArray();

        if (object instanceof List<?>) {
            for(Object o : ((List) object)) {
                jsonArray.add(toJson(o));
            }
        } else if(object instanceof Object[]) {
            for(Object o : ((Object[]) object)) {
                jsonArray.add(toJson(o));
            }
        } else if (object instanceof HashMap<?,?>) {
            jsonArray.add(jsonMap((HashMap<?, ?>) object));
        }else {
            jsonArray.add(object);
        }

        return jsonArray;
    }

}
