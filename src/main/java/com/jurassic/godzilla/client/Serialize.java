package com.jurassic.godzilla.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class Serialize {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        MessageBody messageBody = new MessageBody();
        messageBody.setBodyType(BodyType.FOWARD_DESTINATION);
        messageBody.setFromId("clientId");
        messageBody.setToId("anotherClientId");
        messageBody.setProtocolType(ProtocolType.WEBSOCKET);
        messageBody.setRefCount(0);

        Map<String, Object> data = new DataBuilder()
                .measurement("tableName")
                .fieldNames("field1", "field2")
                .tagNames("tag1", "tag2")
                .field("field1", "fieldValue1")
                .field("field2", "fieldValue2")
                .tag("tag1", Long.valueOf("11111111111111"))
                .tag("tag2", "tagValue2")
                .simpleValidate()
                .strictlyValidate()
                .create();
        messageBody.setData(gson.toJson(data));

        String jsonStr = gson.toJson(messageBody);
        System.out.println(jsonStr);
    }
}
