package com.jurassic.godzilla.client;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataBuilder {
    private Map<String, Object> kv = new HashMap<>();
    private Map<String, Object> fieldMap = new HashMap<>();
    private Map<String, Object> tagMap = new HashMap<>();
    private String measurement;
    private String[] fieldNames;
    private String[] tagNames;

    public DataBuilder field(String fieldName, Object fieldValue) {
        fieldMap.put(fieldName, fieldValue);
        return this;
    }

    public DataBuilder fieldNames(String... fieldNames) {
        this.fieldNames = fieldNames;
        return this;
    }

    public DataBuilder tagNames(String... tagNames) {
        this.tagNames = tagNames;
        return this;
    }

    public DataBuilder tag(String tagName, Object tagValue) {
        tagMap.put(tagName, tagValue);
        return this;
    }

    public DataBuilder measurement(String measurement) {
        this.measurement = measurement;
        return this;
    }

    public Map<String, Object> create() {
        kv.put("measurement", measurement);
        kv.put("tags", StringUtils.joinWith(",", tagNames));
        kv.put("fields", StringUtils.joinWith(",", fieldNames));
        kv.putAll(fieldMap);
        kv.putAll(tagMap);
        return kv;
    }

    public DataBuilder simpleValidate() {
        if (ArrayUtils.isEmpty(fieldNames) || fieldMap.size() == 0) {
            throw new IllegalArgumentException("Field's name or value is invalid.");
        }
        if (fieldNames.length != fieldMap.size()) {
            throw new IllegalArgumentException("Field's name not match value.");
        }
        if (ArrayUtils.isNotEmpty(tagNames) && tagMap.size() == 0) {
            throw new IllegalArgumentException("Tag's name or value is invalid.");
        }
        if (tagNames.length != tagMap.size()) {
            throw new IllegalArgumentException("Tag's name not match value.");
        }
        return this;
    }

    public DataBuilder strictlyValidate() {
        simpleValidate();
        if (CollectionUtils.containsAll(Arrays.asList(tagNames), tagMap.keySet())) ;
        for (String tagName : tagNames) {
            if (null == tagMap.get(tagName)) {
                throw new IllegalArgumentException("Tag's name not match value.");
            }
        }

        for (String fieldName : fieldNames) {
            if (null == fieldMap.get(fieldName)) {
                throw new IllegalArgumentException("Field's name not match value.");
            }
        }

        return this;
    }
}
