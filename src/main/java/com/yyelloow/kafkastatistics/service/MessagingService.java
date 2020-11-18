package com.yyelloow.kafkastatistics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyelloow.kafkastatistics.messaging.LoginMessage;
import com.yyelloow.kafkastatistics.messaging.RegistrationMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MessagingService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    private final Logger logger= LoggerFactory.getLogger(MessagingService.class);

    public void sendRegistrationMessage(RegistrationMessage msg) throws IOException {
        logger.info("sendRegistrationMessage");
        send("topic_registration", msg);
    }

    public void sendLoginMessage(LoginMessage msg) throws IOException {
        logger.info("sendLoginMessage");
        send("topic_login", msg);
    }

    private void send(String topic, Object msg) throws IOException {
        ProducerRecord<String, String> pr = new ProducerRecord<>(topic, objectMapper.writeValueAsString(msg));
        pr.headers().add("type", msg.getClass().getName().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(pr);
    }
}
