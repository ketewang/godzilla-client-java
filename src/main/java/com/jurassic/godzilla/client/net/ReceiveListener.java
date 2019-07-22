package com.jurassic.godzilla.client.net;

public interface ReceiveListener extends Listener {
    void onMessage(String message);
}
