package it.ctinnovation.quartzdemo.job;

import it.ctinnovation.quartzdemo.payload.MqttKeyValue;
import it.ctinnovation.quartzdemo.payload.MqttMessage;
import it.ctinnovation.quartzdemo.service.MqttGateway;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Job di invio del messaggio gestito dallo scheduler
 */
public class MqttJob extends QuartzJobBean implements Job {
    private static final Logger logger = LoggerFactory.getLogger(MqttJob.class);

    @Autowired
    MqttGateway mqttGateway;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        // predisposizione del messaggio da inviare al broker mqtt
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String topic=jobDataMap.getString("topic");
        String name=jobDataMap.getString("name");
        String mqttMessage=jobDataMap.getString("mqttMessage");

        logger.debug("Topic: {} - Name: {} ", topic,name,mqttMessage);

        // invio del messaggio al broker
        MqttMessage message= new MqttMessage();
        message = (MqttMessage) message.jaxbUnmarshall(mqttMessage);
        String jsonMessage=message.jaxbJsonMarshall();

        // invio del messaggio
        mqttGateway.sendToMqtt(jsonMessage, topic);

        logger.debug("Messaggio inviato a mqtt broker: \n{}",jsonMessage);
    }
}
