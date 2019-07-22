package com.jurassic.godzilla.client;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private String fromId;
    private String toId;
    private int refCount;
    private byte protocolType;
    private byte bodyType;
    private String data;
    private String appId;
}
