package com.jurassic.godzilla.client.net;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebsocketClientBuilder {
    private WebSocketClient client;
    private GodzillaWebSocketClient godzillaWebSocketClient;
    public static final AtomicBoolean isOpen = new AtomicBoolean(true);
    private static final String DEFAULT_PORT = "8081";
    private static final String DEFAULT_PATH = "/ws";

    private String host;
    private String port;
    private String path;

    private CloseListener closeListener;
    private ErrorListener errorListener;
    private OpenListener openListener;
    private ReceiveListener receiveListener;

    public WebsocketClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public WebsocketClientBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    public WebsocketClientBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public WebsocketClientBuilder setHostDefault(String host) {
        this.host = host;
        this.port = DEFAULT_PORT;
        this.path = DEFAULT_PATH;
        return this;
    }

    public WebsocketClientBuilder addListenerOnOpen(final OpenListener openListener) {
        this.openListener = openListener;
        return this;
    }

    public WebsocketClientBuilder addListenerOnMessage(final ReceiveListener receiveListener) {
        this.receiveListener = receiveListener;
        return this;
    }

    public WebsocketClientBuilder addListenerOnClose(final CloseListener closeListener) {
        this.closeListener = closeListener;
        return this;
    }

    public WebsocketClientBuilder addListenerOnError(final ErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public GodzillaWebSocketClient create() throws URISyntaxException {
        final OpenListener openListener = this.openListener;
        final CloseListener closeListener = this.closeListener;
        final ErrorListener errorListener = this.errorListener;
        final ReceiveListener receiveListener = this.receiveListener;

        return create(openListener, closeListener, errorListener, receiveListener);
    }

    public GodzillaWebSocketClient create(final OpenListener openListener, final CloseListener closeListener, final ErrorListener errorListener, final ReceiveListener receiveListener) throws URISyntaxException {
        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://" + this.host + ":" + this.port + this.path)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                if (openListener != null) {
                    openListener.onOpen(handshakedata);
                }
            }

            @Override
            public void onMessage(String message) {
                if (receiveListener != null) {
                    receiveListener.onMessage(message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (closeListener != null) {
                    closeListener.onClose(code, reason, remote);
                }
            }

            @Override
            public void onError(Exception ex) {
                if (errorListener != null) {
                    errorListener.onError(ex);
                }
            }
        };
        godzillaWebSocketClient = new GodzillaWebSocketClient(webSocketClient);
        return godzillaWebSocketClient;
    }
}
