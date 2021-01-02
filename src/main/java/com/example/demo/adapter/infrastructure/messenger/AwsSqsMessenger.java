package com.example.demo.adapter.infrastructure.messenger;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.example.demo.app.messenger.UserMessenger;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AwsSqsMessenger implements UserMessenger {

    private final AmazonSQS amazonSQS;
    private final String sqsQueueURL;
    private final Integer MESSAGE_TOP = 0;

    @Autowired
    public AwsSqsMessenger(AmazonSQS amazonSQS, @Value("${aws.sqs.queue.url}") String sqsQueueURL) {
        this.amazonSQS = amazonSQS;
        this.sqsQueueURL = sqsQueueURL;
    }


    public void sendMessage(@NonNull User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(sqsQueueURL)
                .withMessageBody(objectMapper.writeValueAsString(user))
                .withDelaySeconds(5);
        SendMessageResult result = amazonSQS.sendMessage(sendMessageRequest);
        log.info("send user message -> {}", result);
    }

    @Override
    public User receiveMessage() throws JsonProcessingException, ResourceNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(sqsQueueURL)
                .withWaitTimeSeconds(5);
        ReceiveMessageResult result = amazonSQS.receiveMessage(receiveMessageRequest);
        log.info("receive user message -> {}", result);

        if (result.getMessages().isEmpty()) {
            throw new ResourceNotFoundException("SQS empty");
        }
        Optional<Message> message = Optional.ofNullable(result.getMessages().get(MESSAGE_TOP));
        if (message.isPresent()) {
            return objectMapper.readValue(message.get().getBody(), User.class);
        }
        throw new ResourceNotFoundException("SQS empty");
    }
}
