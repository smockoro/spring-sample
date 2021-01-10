/**
 * @formatter:off
 * MIT License
 *
 * Copyright (c) 2021 mockoro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * @formatter:on
 */
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
    private final ObjectMapper objectMapper;

    @Autowired
    public AwsSqsMessenger(AmazonSQS amazonSQS, @Value("${aws.sqs.queue.url}") String sqsQueueURL,
                           ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.sqsQueueURL = sqsQueueURL;
        this.objectMapper = objectMapper;
    }


    public void sendMessage(@NonNull User user) throws JsonProcessingException {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(sqsQueueURL)
                .withMessageBody(this.objectMapper.writeValueAsString(user))
                .withDelaySeconds(5);
        SendMessageResult result = amazonSQS.sendMessage(sendMessageRequest);
        log.info("send user message -> {}", result);
    }

    @Override
    public User receiveMessage() throws JsonProcessingException, ResourceNotFoundException {
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
            return this.objectMapper.readValue(message.get().getBody(), User.class);
        }
        throw new ResourceNotFoundException("SQS empty");
    }
}
