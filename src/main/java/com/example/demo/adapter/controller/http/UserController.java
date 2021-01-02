package com.example.demo.adapter.controller.http;

import com.example.demo.app.usecase.UserUsecase;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final UserUsecase userUsecase;

    @Autowired
    public UserController(UserUsecase userUsecase) {
        this.userUsecase = userUsecase;
    }

    @GetMapping("/users")
    public List<User> GetUser() {
        return this.userUsecase.getUser();
    }

    @GetMapping("/users/{id}")
    public User getOneUser(@PathVariable(name = "id") String id) throws ResourceNotFoundException {
        return this.userUsecase.getUserById(id);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return this.userUsecase.createUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(name = "id") String id) throws ResourceNotFoundException {
        this.userUsecase.deleteUserById(id);
    }

    @PostMapping("/users/{id}/send")
    public void postUserToSqs(@PathVariable(name = "id") String id) throws ResourceNotFoundException, JsonProcessingException {
        this.userUsecase.sendUserToQueue(id);
    }

    @GetMapping("/users/receive")
    public User receiveFromSqs() throws ResourceNotFoundException, JsonProcessingException {
        return this.userUsecase.receiveUserFromQueue();
    }
}
