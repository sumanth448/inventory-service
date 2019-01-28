package com.paytm.inventory.models.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Properties {
    @JsonIgnore
    Map<String, Object> properties = new HashMap<>();

    @JsonAnySetter
    public void addToAdditionalProperties(String key, Object value) {
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return properties;
    }
}
