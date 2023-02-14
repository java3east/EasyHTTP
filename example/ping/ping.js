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