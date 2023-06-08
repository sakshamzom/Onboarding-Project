package com.example.sync.kafka;

import com.example.sync.model.User;
import com.example.sync.model.UserWithReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class JsonKafkaProducer {
    private KafkaTemplate<String, User> kafkaTemplate;


    public JsonKafkaProducer(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(UserWithReq data)
    {
        Message<UserWithReq> message= MessageBuilder.withPayload(data).setHeader(KafkaHeaders.TOPIC,"javaguides_json_2").build();
        ListenableFuture<SendResult<String, User>> send =kafkaTemplate.send(message);

    }
}
