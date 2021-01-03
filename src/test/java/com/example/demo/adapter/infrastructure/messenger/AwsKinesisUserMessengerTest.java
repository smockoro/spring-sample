package com.example.demo.adapter.infrastructure.messenger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest;
import software.amazon.awssdk.services.kinesis.model.CreateStreamResponse;
import software.amazon.awssdk.services.kinesis.model.DeleteStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DeleteStreamResponse;
import software.amazon.kinesis.common.KinesisClientUtil;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class AwsKinesisUserMessengerTest {

    private static KinesisAsyncClient kinesisAsyncClient;

    @BeforeAll
    static void createStream() throws ExecutionException, InterruptedException {
        AwsCredentials credentials = AwsBasicCredentials.create("dummy", "dummy");
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        kinesisAsyncClient = KinesisClientUtil.createKinesisAsyncClient(
                KinesisAsyncClient.builder()
                        .credentialsProvider(credentialsProvider)
                        .region(Region.AP_NORTHEAST_1)
                        .endpointOverride(URI.create("http://localhost:4566"))
        );

        CompletableFuture<CreateStreamResponse> completableFuture = kinesisAsyncClient.createStream(
                CreateStreamRequest.builder().streamName("test-stream").shardCount(10).build());
        completableFuture.get();
        completableFuture.whenComplete((ret, ex) -> {
            if (ex == null) {
                System.out.println("completed create stream: " + ret.toString());
            } else {
                ex.printStackTrace();
            }
        });
    }

    @AfterAll
    static void deleteStream() throws ExecutionException, InterruptedException {
        CompletableFuture<DeleteStreamResponse> completableFuture = kinesisAsyncClient.deleteStream(
                DeleteStreamRequest.builder().streamName("test-stream").build());
        completableFuture.get();
        completableFuture.whenComplete((ret, ex) -> {
            if (ex == null) {
                System.out.println("completed delete stream: " + ret.toString());
            } else {
                ex.printStackTrace();
            }
        });
    }

    @Test
    void sendMessage() {
        System.out.println("test send message");
    }

    @Test
    void receiveMessage() {
        System.out.println("test reveive Message");
    }
}