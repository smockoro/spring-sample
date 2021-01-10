package com.example.demo.app.usecase;

import com.example.demo.domain.entity.Item;

import java.util.List;

public interface ItemUsecase {
    List<Item> getAllItems();
    Item getItemById(Long id);
    Long createItem(List<Item> items);
    Boolean deleteItems();
    Long deleteItemById(Long id);
}
