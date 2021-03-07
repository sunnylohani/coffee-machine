package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.exception.CoffeeMachineException;
import com.dunzo.coffeemachine.model.Beverage;

public interface CoffeeMachineService {
    void placeOrder(Beverage beverage) throws CoffeeMachineException;
}
