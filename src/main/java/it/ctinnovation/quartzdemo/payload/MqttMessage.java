package it.ctinnovation.quartzdemo.payload;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement
public class MqttMessage implements MessageMarshaller{
    String name;
    private List<MqttKeyValue> data = new ArrayList<>();
}
