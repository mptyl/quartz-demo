package it.ctinnovation.quartzdemo.payload;

import lombok.Data;

@Data
public class ScheduledMqttResponse {
    private boolean success;
    private String jobId;
    private String jobGroup;
    private String message;

    public ScheduledMqttResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScheduledMqttResponse(boolean success, String jobId, String jobGroup, String message) {
        this.success = success;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
        this.message = message;
    }

}
