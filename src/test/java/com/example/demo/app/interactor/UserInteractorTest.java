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
package com.example.demo.app.interactor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.example.demo.adapter.infrastructure.messenger.AwsSqsMessenger;
import com.example.demo.adapter.infrastructure.repository.InMemoryUserRepository;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.helper.UserAggregatorTestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserInteractorTest {

  @Mock
  InMemoryUserRepository userRepository;

  @Mock
  AwsSqsMessenger userMessenger;

  @InjectMocks
  UserInteractor userInteractor;

  @BeforeEach
  void initMock() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("get users")
  void getUser() {
    List<User> expectedUsers = new ArrayList<>();
    doReturn(expectedUsers).when(userRepository).selectAll();
    List<User> actualUsers = userInteractor.getUser();
    assertEquals(expectedUsers, actualUsers);
  }

  @ParameterizedTest
  @DisplayName("get user by id")
  @CsvSource({
      "1,sample,18",
      "2,,18",
      "3,sample,",
      "4,,",
  })
  void getUserById(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser)
      throws ResourceNotFoundException {
    doReturn(expectedUser).when(userRepository).selectById(expectedUser.getId().toString());
    User actualUser = userInteractor.getUserById(expectedUser.getId().toString());
    assertEquals(expectedUser, actualUser);
  }

  @Test
  @DisplayName("get user by id but resource not found exception")
  void getUserByIdButResourceNotFound() throws ResourceNotFoundException {
    doThrow(ResourceNotFoundException.class).when(userRepository).selectById(anyString());
    assertThrows(ResourceNotFoundException.class, () -> userInteractor.getUserById("1"));
  }

  @ParameterizedTest
  @DisplayName("get user by id")
  @CsvSource({
      "1,sample,18",
      "2,,18",
      "3,sample,",
      "4,,",
  })
  void createUser(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser) {
    doReturn(expectedUser).when(userRepository).insert(expectedUser);
    User actualUser = userInteractor.createUser(expectedUser);
    assertEquals(expectedUser, actualUser);
  }

  @Test
  void deleteUserById() throws ResourceNotFoundException {
    doNothing().when(userRepository).deleteById(anyString());
    assertDoesNotThrow(() -> userInteractor.deleteUserById("1"));
  }

  @Test
  @DisplayName("delete user by id but resource not found exception")
  void deleteUserByIdButResourceNotFound() throws ResourceNotFoundException {
    doThrow(ResourceNotFoundException.class).when(userRepository).deleteById(anyString());
    assertThrows(ResourceNotFoundException.class, () -> userInteractor.deleteUserById("1"));
  }

  @ParameterizedTest
  @DisplayName("send user to queue")
  @CsvSource({
      "1,sample,18",
  })
  void sendUserToQueue(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser)
      throws ResourceNotFoundException, JsonProcessingException {
    doReturn(expectedUser).when(userRepository).selectById(expectedUser.getId().toString());
    doNothing().when(userMessenger).sendMessage(expectedUser);
    assertDoesNotThrow(() -> userInteractor.sendUserToQueue(expectedUser.getId().toString()));
  }

  @Test
  @DisplayName("send user to queue but occur Exceptions")
  void sendUserToQueueButExceptions()
      throws ResourceNotFoundException, JsonProcessingException {
    User user = new User(2L, "adam", "19");
    doThrow(ResourceNotFoundException.class).when(userRepository).selectById("1");
    doReturn(user).when(userRepository).selectById(user.getId().toString());

    doThrow(JsonProcessingException.class).when(userMessenger).sendMessage(user);

    assertThrows(ResourceNotFoundException.class, () -> userInteractor.sendUserToQueue("1"));
    assertThrows(JsonProcessingException.class, () -> userInteractor.sendUserToQueue("2"));
  }

  @ParameterizedTest
  @DisplayName("receive user form queue")
  @CsvSource({
      "1,sample,18",
  })
  void receiveUserFromQueue(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser)
      throws ResourceNotFoundException, JsonProcessingException {
    doReturn(expectedUser).when(userMessenger).receiveMessage();
    User actualUser = userInteractor.receiveUserFromQueue();
    assertEquals(expectedUser, actualUser);
  }

  @Test
  @DisplayName("receive user from queue but occur exceptions")
  void receiveUserFromQueueButExceptions()
      throws ResourceNotFoundException, JsonProcessingException {
    doThrow(JsonProcessingException.class).when(userMessenger).receiveMessage();
    assertThrows(JsonProcessingException.class, () -> userInteractor.receiveUserFromQueue());

    doThrow(ResourceNotFoundException.class).when(userMessenger).receiveMessage();
    assertThrows(ResourceNotFoundException.class, () -> userInteractor.receiveUserFromQueue());
  }
}