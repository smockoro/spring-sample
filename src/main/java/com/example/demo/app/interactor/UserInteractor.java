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
