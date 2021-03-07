package com.dunzo.coffeemachine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Beverage {

    private String name;
    private Map<String, Integer> ingredientsQty;
}
