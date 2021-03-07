package com.dunzo.coffeemachine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryResponse {

    private Result result;
    private String message;

    public enum Result {
        SUCCESS, FAILURE
    }
}
