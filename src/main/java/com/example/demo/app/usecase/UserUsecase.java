package com.example.demo.app.usecase;

import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface UserUsecase {
    List<User> getUser();
    User getUserById(String id) throws ResourceNotFoundException;
    User createUser(User user);
    void deleteUserById(String id) throws ResourceNotFoundException;
    void sendUserToQueue(String id) throws ResourceNotFoundException, JsonProcessingException;
    User receiveUserFromQueue() throws ResourceNotFoundException, JsonProcessingException;
}
