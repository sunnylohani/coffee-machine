package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.exception.CoffeeMachineException;
import com.dunzo.coffeemachine.model.Beverage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
@AllArgsConstructor
public class CoffeeMachineServiceImpl implements CoffeeMachineService {

    private final BlockingQueue<Beverage> orderQueue;

    @Override
    public void placeOrder(Beverage beverage) throws CoffeeMachineException {
        try {
            orderQueue.put(beverage);
            log.debug("Order placed for beverage {}", beverage);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Order cannot be placed for beverage {}", beverage, e);
            throw new CoffeeMachineException("Order cannot be placed", e);
        }
    }
}
