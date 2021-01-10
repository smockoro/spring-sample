package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
