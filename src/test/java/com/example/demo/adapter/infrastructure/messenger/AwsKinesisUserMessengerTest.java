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