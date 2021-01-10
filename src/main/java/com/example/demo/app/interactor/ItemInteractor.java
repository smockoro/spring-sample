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

import com.example.demo.adapter.infrastructure.repository.MongoItemRepository;
import com.example.demo.app.repository.ItemRepository;
import com.example.demo.app.usecase.ItemUsecase;
import com.example.demo.domain.entity.Item;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemInteractor implements ItemUsecase {

  private final ItemRepository itemRepository;

  public ItemInteractor(MongoItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Override
  public List<Item> getAllItems() {
    return null;
  }

  @Override
  public Item getItemById(Long id) {
    return null;
  }

  @Override
  public Long createItem(List<Item> items) {
    return null;
  }

  @Override
  public Boolean deleteItems() {
    return true;
  }

  @Override
  public Long deleteItemById(Long id) {
    return null;
  }
}
