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
package com.example.demo.adapter.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.example.demo.adapter.infrastructure.repository.helper.LongSerialNumberGenerator;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.helper.UserAggregatorTestHelper;
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

class InMemoryUserRepositoryTest {

  @Mock
  LongSerialNumberGenerator serialNumberGenerator;

  @InjectMocks
  InMemoryUserRepository repository;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("hash map all display")
  void selectAll() {
    List<User> expected = new ArrayList<>();
    List<User> actual = repository.selectAll();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("not found and occur exception")
  void selectById() {
    assertThrows(ResourceNotFoundException.class, () -> repository.selectById("1"));
  }

  @ParameterizedTest
  @DisplayName("create user in memory hash map")
  @CsvSource({
      "0,sample,18",
      ",sample,18",
      "2,,18",
      "3,sample,",
      "4,,",
  })
  void insert(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser) {
    if (expectedUser.getId() == null) {
      expectedUser.setId(0L);
    }
    doReturn(expectedUser.getId()).when(serialNumberGenerator).generate();
    User actualUser = repository.insert(expectedUser);
    assertEquals(expectedUser, actualUser);
  }

  @Test
  void deleteById() {
    assertThrows(ResourceNotFoundException.class, () -> repository.deleteById("1"));
  }
}