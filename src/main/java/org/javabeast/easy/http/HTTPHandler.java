package org.javabeast.easy.http;

import org.jetbrains.annotations.NotNull;

/**
 * Handles HTTPRequests
 * @author Java3east
 * @version 1.0
 */
public interface HTTPHandler {

    /**
     * Handle the Request
     * @param request the request to be handled
     * @return the result, which will be returned to the client
     */
    @NotNull HTTPResult handle(@NotNull HTTPRequest request);

}
