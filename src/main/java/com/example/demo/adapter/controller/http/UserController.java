package com.example.demo.adapter.controller.http;

import com.example.demo.app.usecase.UserUsecase;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserUsecase userUsecase;

    @Autowired
    public UserController(UserUsecase userUsecase) {
        this.userUsecase = userUsecase;
    }

    @GetMapping("/users")
    public List<User> GetUser() {
        List<User> users = this.userUsecase.getUser();
        return users;
    }

    @GetMapping("/users/{id}")
    public User getOneUser(@PathVariable(name = "id", required = true) String id) throws ResourceNotFoundException {
        User user = this.userUsecase.getUserById(id);
        return user;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody(required = true) User user) {
        User createdUser = this.userUsecase.createUser(user);
        return createdUser;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(name = "id", required = true) String id) throws ResourceNotFoundException {
        this.userUsecase.deleteUserById(id);
    }

    @PostMapping("/users/{id}/send")
    public void postUserToSqs(@PathVariable(name = "id", required = true) String id) throws ResourceNotFoundException, JsonProcessingException {
        this.userUsecase.sendUserToQueue(id);
    }

    @GetMapping("/users/receive")
    public void receiveFromSqs() {
        this.userUsecase.receiveUserFromQueue();
    }
}
