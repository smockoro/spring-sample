package com.example.demo.adapter.controller.http;

import com.example.demo.app.usecase.ItemUsecase;
import com.example.demo.domain.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemUsecase itemUsecase;

    @Autowired
    public ItemController(ItemUsecase itemUsecase) {
        this.itemUsecase = itemUsecase;
    }

    @GetMapping
    public List<Item> getItems() {
        return this.itemUsecase.getAllItems();
    }

}
