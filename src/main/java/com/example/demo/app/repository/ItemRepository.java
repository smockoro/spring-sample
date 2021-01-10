package com.example.demo.app.repository;

import com.example.demo.domain.entity.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findItems();
    Item findItemById(Long id);
    Long insertItem(List<Item> items);
    Boolean deleteItems();
    Long deleteItemById(Long id);
}
