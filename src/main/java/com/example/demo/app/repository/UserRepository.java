package com.example.demo.app.repository;

import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;

import java.util.List;

public interface UserRepository {
    List<User> selectAll();
    User selectById(String id) throws ResourceNotFoundException;
    User insert(User user);
    void deleteById(String id) throws ResourceNotFoundException;
}
