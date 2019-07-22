package com.jurassic.godzilla.client.net;

import com.jurassic.godzilla.client.BodyType;
import com.jurassic.godzilla.client.JSONUtil;
import com.jurassic.godzilla.client.Message;
import com.jurassic.godzilla.client.ProtocolType;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.Framedata;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GodzillaWebSocketClient implements WebSocket {
    private WebSocketClient webSocketClient;
    private ExecutorService hbExceutorService = Executors.newSingleThreadExecutor();

    public GodzillaWebSocketClient(WebSocketClient webSocketClient) {
        if (webSocketClient == null) {
            throw new IllegalArgumentException("webSocketClient is null!");
        }
        this.webSocketClient = webSocketClient;
    }

    public boolean waitOpen() {
        while (!getReadyState().equals(ReadyState.OPEN)) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public Message crtHBMessage(final String appId, final String fromId) {
        Message message = new Message();
        message.setBodyType(BodyType.HEART_BEAT);
        message.setFromId(fromId);
        message.setToId("");
        message.setProtocolType(ProtocolType.WEBSOCKET);
        message.setRefCount(0);
        message.setAppId(appId);
        message.setData("{\"heartbeat\":\"ok\"}");

        return message;
    }

    public void crtHeartBeat(final String appId, final String fromId, final long interval) {
        hbExceutorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    waitOpen();
                    send(JSONUtil.toJson(crtHBMessage(appId, fromId)));
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
    }

    public void stopHeartBeat() {
        hbExceutorService.shutdownNow();
    }

    @Override
    public void close(int code, String message) {
        stopHeartBeat();
        webSocketClient.close(code, message);
    }

    @Override
    public void close(int code) {
        stopHeartBeat();
        webSocketClient.close(code);
    }

    @Override
    public void close() {
        stopHeartBeat();
        webSocketClient.close();

    }

    @Override
    public void closeConnection(int code, String message) {
        stopHeartBeat();
        webSocketClient.closeConnection(code, message);
    }

    @Override
    public void send(String text) {
        webSocketClient.send(text);
    }

    @Override
    public void send(ByteBuffer bytes) {
        webSocketClient.send(bytes);
    }

    @Override
    public void send(byte[] bytes) {
        waitOpen();
        webSocketClient.send(bytes);
    }

    @Override
    public void sendFrame(Framedata framedata) {
        waitOpen();
        webSocketClient.sendFrame(framedata);
    }

    @Override
    public void sendFrame(Collection<Framedata> frames) {
        waitOpen();
        webSocketClient.sendFrame(frames);
    }

    @Override
    public void sendPing() {
        waitOpen();
        webSocketClient.sendPing();
    }

    @Override
    public void sendFragmentedFrame(Opcode op, ByteBuffer buffer, boolean fin) {
        waitOpen();
        webSocketClient.sendFragmentedFrame(op, buffer, fin);
    }

    @Override
    public boolean hasBufferedData() {
        return webSocketClient.hasBufferedData();
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress() {
        return webSocketClient.getRemoteSocketAddress();
    }

    @Override
    public InetSocketAddress getLocalSocketAddress() {
        return webSocketClient.getLocalSocketAddress();
    }

    @Override
    public boolean isOpen() {
        return webSocketClient.isOpen();
    }

    @Override
    public boolean isClosing() {
        return webSocketClient.isClosing();
    }

    @Override
    public boolean isFlushAndClose() {
        return webSocketClient.isFlushAndClose();
    }

    @Override
    public boolean isClosed() {
        return webSocketClient.isClosed();
    }

    @Override
    public Draft getDraft() {
        return webSocketClient.getDraft();
    }

    @Override
    public ReadyState getReadyState() {
        return webSocketClient.getReadyState();
    }

    @Override
    public String getResourceDescriptor() {
        return webSocketClient.getResourceDescriptor();
    }

    @Override
    public <T> void setAttachment(T attachment) {
        webSocketClient.setAttachment(attachment);
    }

    @Override
    public <T> T getAttachment() {
        return webSocketClient.getAttachment();
    }

    public void connect() {
        webSocketClient.connect();
    }

    public void connectWithHB(final String appId, final String fromId, long interval) {
        webSocketClient.connect();
        crtHeartBeat(appId, fromId, interval);
    }

}
