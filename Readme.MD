# EasyHTTP
This API is made to make the use of Java HttpServers easier.

JavaDoc: http://javabeast.net/java/api/easy/http/javadoc \
download: http://javabeast.net/java/api/easy/http/download

## Ping
### Server
```
import org.javabeast.easy.http.*;
import org.javabeast.easy.http.handlers.*;

public static void main(String[] args) throws Exception{
    // create a new server on port 1997
    HTTPServer httpServer = new HTTPServer(1997);
    // add the default ping listener to the known handlers
    httpServer.registerHandler("/ping", new Ping());
    // start the server
    httpServer.start();
}
```

### client (JavaScript)
```
async function ping() {
    // POST
    const response = await fetch("http://localhost:1997/ping", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            sent: Date.now()
        })
    });

    response.json().then(data => {
        console.log("data: " + JSON.stringify(data));
        console.log("ping: " + data[0].tts + " ms");
    });
}   
ping();
```
