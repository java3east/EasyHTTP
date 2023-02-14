package org.javabeast.easy.http.handlers;

import org.javabeast.easy.http.HTTPHandler;
import org.javabeast.easy.http.HTTPRequest;
import org.javabeast.easy.http.HTTPResult;
import org.javabeast.easy.http.HandlerRule;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;

/**
 * Example HTTPHandler
 * @author Java3east
 * @version 1.0
 * @see org.javabeast.easy.http.HTTPHandler
 */
public class Ping implements HTTPHandler {

    /**
     * @param request the request to be handled
     * @return a new Result containing tts (=timeToServer: the ping)
     */
    @HandlerRule(argument = "sent", type = Long.class) // only run if an argument called 'sent' of type long is withing the request
    @Override
    public @NotNull HTTPResult handle(@NotNull HTTPRequest request) {
        System.out.println("[" + request.ip() + "] pinged!"); // tell the server that it got pinged
        long sent = (long) request.data().get("sent"); // get the timestamp in milliseconds the request was sent
        long tts = new Date().getTime() - sent; // calculate the time it took to reach the server

        // return a new result which is 'OK' and contains the time the package took to reach the server
        return new HTTPResult(200, new HashMap<>() {{
            put("tts", tts);
        }});
    }
}
