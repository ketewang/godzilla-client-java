package com.jurassic.godzilla.client.net;

import org.java_websocket.handshake.ServerHandshake;

public interface OpenListener extends Listener {
    void onOpen(ServerHandshake serverHandshake);
}
