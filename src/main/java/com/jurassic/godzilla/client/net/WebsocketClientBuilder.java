package com.jurassic.godzilla.client.net;

import org.java_websocket.client.WebSocketClient;

import java.util.concurrent.atomic.AtomicBoolean;

public class WebsocketClientBuilder {
    private WebSocketClient client;
    public static final AtomicBoolean isOpen = new AtomicBoolean(true);
    private String host;
    private String port;
    private String path;

    public WebsocketClientBuilder setHost(String host){
        this.host = host;
        return this;
    }

    public WebsocketClientBuilder setPort(String port){
        this.port = port;
        return this;
    }

    public WebsocketClientBuilder setPath(String path){
        this.path = path;
        return this;
    }

}
