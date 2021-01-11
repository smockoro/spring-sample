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

import com.example.demo.adapter.infrastructure.repository.helper.LongSerialNumberGenerator;
import com.example.demo.adapter.infrastructure.repository.helper.SerialNumberGenerator;
import com.example.demo.app.repository.UserRepository;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {

  private final HashMap<Long, User> userHashMap = new HashMap<>();
  private final SerialNumberGenerator serialNumberGenerator;

  @Autowired
  public InMemoryUserRepository(
      LongSerialNumberGenerator serialNumberGenerator) {
    this.serialNumberGenerator = serialNumberGenerator;
  }


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
    log.info(serialNumberGenerator.generate().toString());
    Long generateId = serialNumberGenerator.generate().longValue();
    user.setId(generateId);
    this.userHashMap.put(generateId, user);
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
