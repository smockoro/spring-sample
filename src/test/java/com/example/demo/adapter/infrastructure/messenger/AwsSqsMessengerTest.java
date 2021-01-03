package com.example.demo.adapter.infrastructure.messenger;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.helper.UserAggregatorTestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AwsSqsMessengerTest {

    private static AmazonSQS amazonSQS;
    private static String sendQueueURL;
    private static String receiveQueueURL;
    private static String emptyQueueURL;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setupSqs() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:4566",
                Regions.AP_NORTHEAST_1.getName()
        );

        AWSCredentials credentials = new BasicAWSCredentials(
                "dummy",
                "dummy"
        );
        amazonSQS = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfiguration)
                .build();
        CreateQueueResult createQueueResult = amazonSQS.createQueue("test-queue-send");
        sendQueueURL = createQueueResult.getQueueUrl();
        createQueueResult = amazonSQS.createQueue("test-queue-receive");
        receiveQueueURL = createQueueResult.getQueueUrl();
        createQueueResult = amazonSQS.createQueue("test-queue-empty");
        emptyQueueURL = createQueueResult.getQueueUrl();

        objectMapper = new ObjectMapper();
    }

    @AfterAll
    static void cleanUpSqs() {
        amazonSQS.deleteQueue(sendQueueURL);
        amazonSQS.deleteQueue(receiveQueueURL);
    }

    @ParameterizedTest
    @DisplayName("send message to AWS SQS test")
    @CsvSource({
            "1,sample,18",
            ",sample,18",
            "3,,18",
            "4,sample,",
            ",,",
    })
    void sendMessage(@AggregateWith(UserAggregatorTestHelper.class) User user) {
        AwsSqsMessenger awsSqsMessenger = new AwsSqsMessenger(amazonSQS, sendQueueURL, objectMapper);
        assertDoesNotThrow(() -> awsSqsMessenger.sendMessage(user));
    }

    @ParameterizedTest
    @DisplayName("receiveMessage from AWS SQS test")
    @CsvSource({
            "1,sample,18",
            ",sample,18",
            "3,,18",
            "4,sample,",
            ",,",
    })
    void receiveMessage(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser) {
        try {
            AwsSqsMessenger awsSqsMessenger = new AwsSqsMessenger(amazonSQS, receiveQueueURL, objectMapper);
            sendTestMessageToSqs(receiveQueueURL, expectedUser);
            User actualUser = awsSqsMessenger.receiveMessage();
            assertEquals(expectedUser, actualUser);
        } catch (JsonProcessingException | ResourceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void receiveMessageWithNotFoundException() {
        AwsSqsMessenger awsSqsMessenger = new AwsSqsMessenger(amazonSQS, emptyQueueURL, objectMapper);
        assertThrows(ResourceNotFoundException.class, awsSqsMessenger::receiveMessage);
    }

    private void sendTestMessageToSqs(String receiveQueueURL, User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        amazonSQS.sendMessage(receiveQueueURL, objectMapper.writeValueAsString(user));
    }
}