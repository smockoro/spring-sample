package com.example.demo.adapter.infrastructure.repository;

import com.example.demo.app.repository.ItemRepository;
import com.example.demo.domain.entity.Item;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MongoItemRepository implements ItemRepository {

    @Override
    public List<Item> findItems() {
        return null;
    }

    @Override
    public Item findItemById(Long id) {
        return null;
    }

    @Override
    public Long insertItem(List<Item> items) {
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
