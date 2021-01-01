package com.example.demo.adapter.infrastructure.repository;

import com.example.demo.app.repository.UserRepository;
import com.example.demo.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MysqlUserRepository implements UserRepository {
    @Override
    public List<User> selectAll() {
        return null;
    }

    @Override
    public User selectById(String id) {
        return null;
    }

    @Override
    public User insert(User user) {
        return null;
    }

    @Override
    public void deleteById(String id) {
    }

}
