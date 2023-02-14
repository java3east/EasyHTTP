package org.javabeast.easy.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.javabeast.easy.http.util.json.Json;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * @author Java3east
 * @version 1.0
 */
public class HTTPServer {

    /**
     * The port the server will run on
     */
    private final int port;
    /**
     * The com.sun.net HTTPServer
     */
    private final HttpServer httpServer;
    /**
     * All the registered handlers on this Server
     */
    private final HashMap<String, HTTPHandler> handlers = new HashMap<>();

    /**
     * Create a new HTTPServer
     * @param port the port of the server
     * @throws IOException if the creation of the server fails
     */
    public HTTPServer ( int port ) throws IOException {
        this.port = port;
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.setExecutor(null);
    }

    /**
     * Register a new Handler / Context on this server
     * @see HttpServer#createContext(String, HttpHandler)
     * @param context the context of the handler
     * @param handler the handler, which will handle all requests on this context
     */
    public void registerHandler(String context, HTTPHandler handler) {
        handlers.put(context, handler);
        httpServer.createContext(context, exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            System.out.println("receiving request: '" + context + "'");
            try {
                HTTPResult result = new HTTPResult(HTTPResult.INTERNAL_SERVER_ERROR, new HashMap<>());

                HTTPHandler httpHandler = handlers.get(context);
                HashMap<String, Class<?>> params = new HashMap<>();
                Method method = httpHandler.getClass().getMethod("handle", HTTPRequest.class);
                HandlerRule[] rules = method.getAnnotationsByType(HandlerRule.class);
                for (HandlerRule handlerRule : rules) {
                    params.put(handlerRule.argument(), handlerRule.type());
                }

                String ip = exchange.getRemoteAddress().getAddress().toString().replace("0:0:0:0:0:0:0:1", "127.0.0.1");
                HashMap<Object, Object> data = new HashMap<>();

                System.out.println("METHOD: '" + exchange.getRequestMethod() + "'");

                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    InputStream inputStream = exchange.getRequestBody();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String _data_ = new String(dataInputStream.readAllBytes());

                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(_data_);
                    data = Json.toHashMap(jsonObject);

                } else if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    data = queryToMap(exchange.getRequestURI().getQuery());
                }

                for (String key : params.keySet()) {
                    if (data.containsKey(key)) {
                        if (data.get(key).getClass().equals(params.get(key))) {
                            result = httpHandler.handle(new HTTPRequest(ip, data, exchange));
                        } else {
                            HashMap<Object, Object> finalData = data;
                            result = new HTTPResult(HTTPResult.BAD_REQUEST, new HashMap<>() {{
                                put("errors", new String[]{"key '" + key + "' of wrong type: expected '" + params.get(key).getSimpleName() + "' got '" + finalData.get(key).getClass().getSimpleName() + "'"});
                            }});
                            System.out.println("REQUEST FAILED: " + key + "' + key + ' of wrong type: expected '" + params.get(key).getSimpleName() + "' got '" + finalData.get(key).getClass().getSimpleName() + "'");
                        }
                    } else {
                        result = new HTTPResult(HTTPResult.BAD_REQUEST, new HashMap<>() {{
                            put("errors", new String[]{"missing key '" + key + "'"});
                        }});
                        System.out.println("REQUEST FAILED: missing key '" + key + "'");
                    }
                }
                if (params.size() == 0) {
                    result = httpHandler.handle(new HTTPRequest(ip, data, exchange));
                }

                String response = result.toJson().toJSONString();
                exchange.sendResponseHeaders(result.code(), response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }catch (Exception exception) {
                exception.printStackTrace();
                String response = new HTTPResult(HTTPResult.INTERNAL_SERVER_ERROR, new HashMap<>()).toJson().toJSONString();
                exchange.sendResponseHeaders(HTTPResult.INTERNAL_SERVER_ERROR, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
    }

    /**
     * convert the 'GET' arguments into a HashMap
     * @param query the 'GET' arguments as string
     * @return the HashMap containing the arguments
     */
    private @NotNull HashMap<Object, Object> queryToMap(@Nullable String query) {
        if(query == null) {
            return new HashMap<>();
        }
        HashMap<Object, Object> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    /**
     * Start the HTTPServer
     * @see HttpServer#start() 
     */
    public void start() {
        if (httpServer != null) httpServer.start();
    }

    /**
     * Stop the HTTPServer
     * @see HttpServer#stop(int)
     */
    public void stop() {
        if (httpServer == null) httpServer.stop(0);
    }

    /**
     * get the port of this server
     * @return the port this http server will run on
     */
    public int getPort() {
        return port;
    }

}
