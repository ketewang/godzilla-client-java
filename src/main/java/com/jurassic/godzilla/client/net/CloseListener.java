package com.jurassic.godzilla.client.net;

public interface CloseListener extends Listener {
    void onClose(int code, String reason, boolean remote);
}
