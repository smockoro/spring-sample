package com.example.demo.adapter.infrastructure.messenger;

import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.UserRecordResult;
import com.example.demo.app.messenger.UserMessenger;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;

@Slf4j
public class AwsKinesisUserMessenger implements UserMessenger {

    private final ObjectMapper objectMapper;

    private final ExecutorService callbackThreadPool;

    private final KinesisProducer kinesisProducer;
    private final FutureCallback<UserRecordResult> producerCallback = new FutureCallback<>() {

        @Override
        public void onSuccess(@Nullable UserRecordResult result) {
            log.info("{}", result);
        }

        @Override
        public void onFailure(Throwable t) {
            log.error(t.getMessage());
        }

    };

    @Autowired
    public AwsKinesisUserMessenger(ObjectMapper objectMapper, ExecutorService callbackThreadPool,
                                   KinesisProducer kinesisProducer) {
        this.objectMapper = objectMapper;
        this.callbackThreadPool = callbackThreadPool;
        this.kinesisProducer = kinesisProducer;
    }

    @Override
    public void sendMessage(@NonNull User user) throws JsonProcessingException {
        ByteBuffer data = ByteBuffer.wrap(objectMapper.writeValueAsBytes(user));
        ListenableFuture<UserRecordResult> future = kinesisProducer.addUserRecord("sample-stream",
                "sample-partition-key",
                data);
        Futures.addCallback(future, this.producerCallback, callbackThreadPool);
    }

    @Override
    public User receiveMessage() throws JsonProcessingException, ResourceNotFoundException {
        return null;
    }

}
