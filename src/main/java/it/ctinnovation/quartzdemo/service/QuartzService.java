package it.ctinnovation.quartzdemo.service;

import it.ctinnovation.quartzdemo.job.MqttJob;
import it.ctinnovation.quartzdemo.payload.ScheduledMqttRequest;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuartzService {
    private static final Logger logger = LoggerFactory.getLogger(QuartzService.class);
    /**
     * Costruzione del messaggio Mqtt salveto come topic, name (del dispositivo a cui mangiare il messaggio)
     * e mappa (di mappa) nel record Job di Qauartz. I dati nella mappa sono inviati al server Mqtt
     *
     * @param scheduledMqttRequest
     * @return
     */
    public JobDetail buildJobDetail(ScheduledMqttRequest scheduledMqttRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("topic", scheduledMqttRequest.getTopic());
        jobDataMap.put("name", scheduledMqttRequest.getMqttMessage().getName());
        jobDataMap.put("mqttMessage", scheduledMqttRequest.getMqttMessage().jaxbMarshall());

        return JobBuilder.newJob(MqttJob.class)
                .withIdentity(UUID.randomUUID().toString(), "mqtt-messages")
                .withDescription("Send Mqtt Message Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    /**
     * Configurazione del Trigger che invia i messaggi MQTT schedulati tramite Simple Scheduler
     *
     * @param jobDetail
     * @return
     */
    public Trigger buildJobTrigger(ScheduledMqttRequest scheduledMqttRequest, JobDetail jobDetail) {
        Trigger trigger= TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "mqtt-triggers")
                .withDescription("Send Mqtt Message Trigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withRepeatCount(scheduledMqttRequest.getRepeatCount())
                        .withIntervalInSeconds(scheduledMqttRequest.getIntervalInSeconds()))
                .build();
        return trigger;
    }

    /**
     * Metodo di costruzione del Trigger che invia i messaggi MQTT schedulati tramite Cron Scheduler
     *
     * @param scheduledMqttRequest
     * @param jobDetail
     * @return
     */
    public CronTrigger buildCronJobTrigger(ScheduledMqttRequest scheduledMqttRequest, JobDetail jobDetail) {
        CronTrigger trigger= (CronTrigger) TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName(), "mqtt-triggers")
            .withDescription("Send Cron Scheduled Mqtt Message Trigger")
            .withSchedule(createSchedule(scheduledMqttRequest.getCron()))
            .build();
        return trigger;
    }

    private static ScheduleBuilder createSchedule(String cronExpression){
        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(cronExpression);
        return builder;
    }
}
