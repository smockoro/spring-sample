package com.example.demo.app.interactor;

import com.example.demo.app.repository.ItemRepository;
import com.example.demo.app.usecase.ItemUsecase;
import com.example.demo.domain.entity.Item;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ItemInteractor implements ItemUsecase {
    private final ItemRepository itemRepository;

    public ItemInteractor(ItemRepository itemRepository) {
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
        return null;
    }

    @Override
    public Long deleteItemById(Long id) {
        return null;
    }
}
