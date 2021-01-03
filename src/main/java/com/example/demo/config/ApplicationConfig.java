package com.example.demo.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.producer.IKinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {

    @Bean
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AmazonSQS createAmazonSqsClient() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();

        AWSCredentials credentials = new BasicAWSCredentials(
                "dummy",
                "dummy"
        );

        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfiguration)
                .build();
        return amazonSQS;
    }

    @Bean
    public IKinesisProducer createKinesisProducer() {
        AWSCredentials credentials = new BasicAWSCredentials(
                "dummy",
                "dummy"
        );

        KinesisProducerConfiguration producerConfiguration = new KinesisProducerConfiguration()
                .setCredentialsProvider(new AWSStaticCredentialsProvider(credentials))
                .setRegion(Regions.AP_NORTHEAST_1.getName());
        KinesisProducer producer = new KinesisProducer(producerConfiguration);
        return producer;
    }

    @Bean
    public ExecutorService createKinesisCallbackThreadPool() {
        return Executors.newCachedThreadPool();
    }
}
