package org.javabeast.easy.http;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

/**
 * Contains all the data of a Request
 * @author Java3east
 * @version 1.0
 * @param ip the ip of the client sending the request
 * @param data the request data
 * @param exchange the HttpExchange connected to this request
 */
public record HTTPRequest(String ip, HashMap<Object, Object> data, HttpExchange exchange) {

}
