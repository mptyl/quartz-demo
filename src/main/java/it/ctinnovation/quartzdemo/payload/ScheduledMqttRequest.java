package it.ctinnovation.quartzdemo.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
/**
 * Struttura del messaggio che crea un job e ne determina la schedulazione
 */
public class ScheduledMqttRequest {

    @NotNull
    SchedulingType type;

    ///////////////////////////////////////////////////////////////////////////
    // Parametri di cron in caso il type sia CRON
    ///////////////////////////////////////////////////////////////////////////
    @NotNull
    String cron;

    ///////////////////////////////////////////////////////////////////////////
    // Parametri di ripetizione nel caso SchedulingType sia SIMPLE
    ///////////////////////////////////////////////////////////////////////////
    @NotNull
    Integer repeatCount;

    @NotNull
    Integer intervalInSeconds;

    //////////////////////////////////////////////////////////////////////////
    // Messaggio
    //////////////////////////////////////////////////////////////////////////
    @NotNull
    String topic;

    //////////////////////////////////////////////////////////////////////////
    // Lista key-value dei messaggi da inviare
    //////////////////////////////////////////////////////////////////////////
    @NotNull
    MqttMessage mqttMessage;

}
