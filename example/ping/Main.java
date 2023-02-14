import org.javabeast.easy.http.*;
import org.javabeast.easy.http.handlers.*;

public static void main(String[] args) throw Exception{
    // create a new server on port 1997
    HTTPServer httpServer = new HTTPServer(1997);
    // add the default ping listener to the known handlers
    httpServer.registerHandler("/ping", new Ping());
    // start the server
    httpServer.start();
}