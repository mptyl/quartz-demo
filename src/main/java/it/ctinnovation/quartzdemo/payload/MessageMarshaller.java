package it.ctinnovation.quartzdemo.payload;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public interface MessageMarshaller {

    default String jaxbMarshall(){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
            javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(this, sw);
            return sw.toString();
        }
        catch (JAXBException e){
            e.printStackTrace();
        }
        return null;
    }

    default String jaxbJsonMarshall(){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
            javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE,"application/json");
            jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT,false);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(this, sw);
            return sw.toString();
        }
        catch (JAXBException e){
            e.printStackTrace();
        }
        return null;
    }

    default Object jaxbUnmarshall(String r){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return jaxbUnmarshaller.unmarshal(new StringReader(r));
        } catch (JAXBException e){
            e.printStackTrace();
        }
        return null;
    }


}
