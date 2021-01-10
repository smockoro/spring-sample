/**
 * @formatter:off
 * MIT License
 *
 * Copyright (c) 2021 mockoro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * @formatter:on
 */
package com.example.demo.adapter.controller.http;

import com.example.demo.app.usecase.UserUsecase;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

  private final UserUsecase userUsecase;

  @Autowired
  public UserController(UserUsecase userUsecase) {
    this.userUsecase = userUsecase;
  }

  @GetMapping("/users")
  public List<User> getUser() {
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
  public void postUserToSqs(@PathVariable(name = "id") String id)
      throws ResourceNotFoundException, JsonProcessingException {
    this.userUsecase.sendUserToQueue(id);
  }

  @GetMapping("/users/receive")
  public User receiveFromSqs() throws ResourceNotFoundException, JsonProcessingException {
    return this.userUsecase.receiveUserFromQueue();
  }
}
