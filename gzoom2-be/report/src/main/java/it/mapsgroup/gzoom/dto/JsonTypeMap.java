package it.mapsgroup.gzoom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrea Fossi.
 * {@see https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization}
 */
public class JsonTypeMap<K, V> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    @JsonProperty(value = "values")
    private Map<K, V> map;

    public JsonTypeMap(Map<K, V> map) {
        this.map = map;
    }

    public JsonTypeMap() {
        this.map=new HashMap<>();
    }

    public Map<K, V> get() {
        return map ;
    }

    public void set(Map<K, V> map) {
        this.map = map;
    }
}
