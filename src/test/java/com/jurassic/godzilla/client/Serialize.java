package com.jurassic.godzilla.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.Map;

public class Serialize {
    @Test
    public void testMessageFormat(){
        Gson gson = new GsonBuilder().create();
        Message message = new Message();
        message.setBodyType(BodyType.FOWARD_DESTINATION);
        message.setFromId("clientId");
        message.setToId("anotherClientId");
        message.setProtocolType(ProtocolType.WEBSOCKET);
        message.setRefCount(0);
        message.setAppId("appId");

        Map<String, Object> data = new DataBuilder()
                .measurement("tableName")
                .fieldNames("field1", "field2")
                .tagNames("tag1", "tag2")
                .field("field1", "fieldValue1")
                .field("field2", "fieldValue2")
                .tag("tag1", Long.valueOf("11111111111111"))
                .tag("tag2", "tagValue2")
                //检查内容是否合法  可选
                .simpleValidate()
                //检查内容是否合法  可选
                .strictlyValidate()
                .create();
        message.setData(gson.toJson(data));
        String jsonStr = gson.toJson(message);
        System.out.println(jsonStr);
    }
}
