package com.example.demo.adapter.infrastructure.messanger;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.example.demo.app.messanger.UserMessanger;
import com.example.demo.domain.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AwsSqsMessageProducer implements UserMessanger {

    private final AmazonSQS amazonSQS;

    @Autowired
    public AwsSqsMessageProducer(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }


    public void sendMessage(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl("http://localhost:4566/000000000000/foo-queue")
                .withMessageBody(objectMapper.writeValueAsString(user))
                .withDelaySeconds(5);
        SendMessageResult result = amazonSQS.sendMessage(sendMessageRequest);
        System.out.println(result);
    }
}
