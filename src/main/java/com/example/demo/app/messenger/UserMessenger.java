package com.example.demo.app.messenger;

import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;

public interface UserMessenger {
    void sendMessage(@NonNull User user) throws JsonProcessingException;
    User receiveMessage() throws JsonProcessingException, ResourceNotFoundException;
}
