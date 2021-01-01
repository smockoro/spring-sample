package com.example.demo.app.interactor;

import com.example.demo.adapter.infrastructure.messanger.AwsSqsMessageProducer;
import com.example.demo.adapter.infrastructure.repository.InMemoryUserRepository;
import com.example.demo.app.messanger.UserMessanger;
import com.example.demo.app.repository.UserRepository;
import com.example.demo.app.usecase.UserUsecase;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInteractor implements UserUsecase {
    private final UserRepository userRepository;
    private final UserMessanger userMessanger;

    @Autowired
    public UserInteractor(InMemoryUserRepository userRepository,
                          AwsSqsMessageProducer messageProducer) {
        this.userRepository = userRepository;
        this.userMessanger = messageProducer;
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
        User createdUser = this.userRepository.insert(user);
        return createdUser;
    }

    @Override
    public void deleteUserById(String id) throws ResourceNotFoundException {
        this.userRepository.deleteById(id);
    }

    @Override
    public void sendUserToQueue(String id) throws ResourceNotFoundException, JsonProcessingException {
        User user = this.userRepository.selectById(id);
        this.userMessanger.sendMessage(user);
    }
}
