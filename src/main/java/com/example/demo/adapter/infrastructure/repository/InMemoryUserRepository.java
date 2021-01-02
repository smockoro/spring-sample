package com.example.demo.adapter.infrastructure.repository;

import com.example.demo.app.repository.UserRepository;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Long, User> userHashMap = new HashMap<>();
    private Long id = Long.valueOf(0);

    @Override
    public List<User> selectAll() {
        return new ArrayList<>(this.userHashMap.values());
    }

    @Override
    public User selectById(String id) throws ResourceNotFoundException {
        User user = this.userHashMap.get(Long.parseLong(id));
        if (Optional.ofNullable(user).isPresent()) {
            return user;
        } else {
            throw new ResourceNotFoundException("get resource not found");
        }
    }

    @Override
    public User insert(User user) {
        user.setId(this.id);
        this.userHashMap.put(this.id, user);
        this.id++;
        return user;
    }

    @Override
    public void deleteById(String id) throws ResourceNotFoundException {
        if (Optional.ofNullable(this.userHashMap.get(Long.parseLong(id))).isPresent()) {
            this.userHashMap.remove(Long.parseLong(id));
        } else {
            throw new ResourceNotFoundException("deleted resource not found");
        }
    }
}
