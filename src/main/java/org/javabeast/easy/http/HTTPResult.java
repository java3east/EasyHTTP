package org.javabeast.easy.http;

import org.javabeast.easy.http.util.json.Json;
import org.json.simple.JSONArray;

import java.util.HashMap;

/**
 * @author Java3east
 * @version 1.0
 * @param code the response code the server will return to the client
 * @param data the data to return to the client
 */
public record HTTPResult (int code, HashMap<Object, Object> data) {
    /**
     * The package is just fine
     */
    public static final int OK = 200;
    /**
     * Argument missing / of wrong type
     */
    public static final int BAD_REQUEST = 400;
    /**
     * An unhandled exception was thrown
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * Convert the data of this object into an JSONArray
     * @return a JSONArray containing the data of this object
     */
    public JSONArray toJson() {
        return Json.toJSON(data());
    }
}
