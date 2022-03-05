package it.ctinnovation.quartzdemo.payload;

import lombok.Data;

import java.io.Serializable;

@Data
public class MqttKeyValue implements Serializable {
    private String key;
    private String value;
}
