package it.ctinnovation.quartzdemo.controller;

import it.ctinnovation.quartzdemo.payload.ScheduledMqttRequest;
import it.ctinnovation.quartzdemo.payload.ScheduledMqttResponse;
import it.ctinnovation.quartzdemo.payload.SchedulingType;
import it.ctinnovation.quartzdemo.service.QuartzService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MqttScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(MqttScheduleController.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzService quartzService;

    @PostMapping("/scheduleMqtt")
    public ResponseEntity<ScheduledMqttResponse> scheduleEmail(@Valid @RequestBody ScheduledMqttRequest scheduledMqttRequest) {
        try {
            JobDetail jobDetail = quartzService.buildJobDetail(scheduledMqttRequest);
            if(scheduledMqttRequest.getType()== SchedulingType.CRON) {
                CronTrigger trigger = quartzService.buildCronJobTrigger(scheduledMqttRequest, jobDetail);
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                Trigger trigger = quartzService.buildJobTrigger(scheduledMqttRequest, jobDetail);
                scheduler.scheduleJob(jobDetail, trigger);
            }

            ScheduledMqttResponse scheduleMqttResponse = new ScheduledMqttResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Message Scheduled Successfully!");
            return ResponseEntity.ok(scheduleMqttResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling mqtt message", ex);
            ScheduledMqttResponse scheduledMqttResponse = new ScheduledMqttResponse(false,
                    "Error scheduling mqtt message. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduledMqttResponse);
        }
    }

}
