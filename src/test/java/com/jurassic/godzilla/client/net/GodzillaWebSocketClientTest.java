package com.jurassic.godzilla.client.net;

import com.jurassic.godzilla.client.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;


@Ignore
public class GodzillaWebSocketClientTest {

    private GodzillaWebSocketClient godzillaWebSocketClientA;
    private GodzillaWebSocketClient godzillaWebSocketClientB;
    @Before
    public void init(){
        WebsocketClientBuilder builder = new WebsocketClientBuilder();
        builder.setHostDefault("服务器IP")
                .addListenerOnClose((code, reason, remote) -> System.out.println(code+":"+reason+":"+remote))
                .addListenerOnError((e)-> e.printStackTrace())
                .addListenerOnMessage(message -> System.out.println(message))
                .addListenerOnOpen(serverHandshake -> System.out.println(serverHandshake));
        try {
            godzillaWebSocketClientA = new GodzillaWebSocketClient(builder.create());
            godzillaWebSocketClientB = new GodzillaWebSocketClient(builder.create());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connect() {
        godzillaWebSocketClientA.connect();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        godzillaWebSocketClientA.close();
    }

    @Test
    public void connectWithHB() {
        godzillaWebSocketClientA.connectWithHB("test_app","test_fromId",1000);
        try {
            Thread.sleep(10000L);
            godzillaWebSocketClientA.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String crtSendMsg(String appId,String fromId,String toId,String content){
        Message message = new Message();
        message.setBodyType(BodyType.FOWARD_DESTINATION);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setProtocolType(ProtocolType.WEBSOCKET);
        message.setRefCount(0);
        message.setAppId(appId);

        Map<String, Object> data = new DataBuilder()
                .measurement("godzilla_talk")
                .fieldNames("content")
                .tagNames("fromId", "toId")
                .field("content", content)
                .tag("fromId", fromId)
                .tag("toId", toId)
                //检查内容是否合法  可选
                .strictlyValidate()
                .create();
        message.setData(JSONUtil.toJson(data));

        return JSONUtil.toJson(message);
    }

    @Test
    public void sendMsg(){
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                godzillaWebSocketClientA.connectWithHB("chatroom","client_1",1000);
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    godzillaWebSocketClientA.send(crtSendMsg("chatroom","client_1","client_2","hello 1"));
                }
            }
        });
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                godzillaWebSocketClientB.connectWithHB("chatroom","client_2",1000);
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    godzillaWebSocketClientB.send(crtSendMsg("chatroom","client_2","client_1","hello 2"));
                }
            }
        });

        try {
            Thread.sleep(20000);
            godzillaWebSocketClientA.close();
            godzillaWebSocketClientB.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}