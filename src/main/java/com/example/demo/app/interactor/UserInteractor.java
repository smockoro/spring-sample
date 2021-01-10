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
package com.example.demo.app.interactor;

import com.example.demo.adapter.infrastructure.messenger.AwsSqsMessenger;
import com.example.demo.adapter.infrastructure.repository.InMemoryUserRepository;
import com.example.demo.app.messenger.UserMessenger;
import com.example.demo.app.repository.UserRepository;
import com.example.demo.app.usecase.UserUsecase;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserInteractor implements UserUsecase {
    private final UserRepository userRepository;
    private final UserMessenger userMessenger;

    @Autowired
    public UserInteractor(InMemoryUserRepository userRepository,
                          AwsSqsMessenger messageProducer) {
        this.userRepository = userRepository;
        this.userMessenger = messageProducer;
    }

    @Override
    public List<User> getUser() {
        return this.userRepository.selectAll();
    }

    @Override
    public User getUserById(String id) throws ResourceNotFoundException {
        return this.userRepository.selectById(id);
    }

    @Override
    public User createUser(User user) {
        return this.userRepository.insert(user);
    }

    @Override
    public void deleteUserById(String id) throws ResourceNotFoundException {
        this.userRepository.deleteById(id);
    }

    @Override
    public void sendUserToQueue(String id) throws ResourceNotFoundException, JsonProcessingException {
        User user = this.userRepository.selectById(id);
        this.userMessenger.sendMessage(user);
    }

    @Override
    public User receiveUserFromQueue() throws ResourceNotFoundException, JsonProcessingException {
        return this.userMessenger.receiveMessage();
    }
}
