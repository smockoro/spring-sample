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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
