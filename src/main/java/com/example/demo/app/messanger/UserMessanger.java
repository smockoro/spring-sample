package com.example.demo.app.messanger;

import com.example.demo.domain.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserMessanger {
    void sendMessage(User user) throws JsonProcessingException;
}
