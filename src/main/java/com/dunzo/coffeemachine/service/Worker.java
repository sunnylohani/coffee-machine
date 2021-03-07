package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.dao.Inventory;
import com.dunzo.coffeemachine.model.Beverage;
import com.dunzo.coffeemachine.model.InventoryResponse;
import com.dunzo.coffeemachine.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Slf4j
public class Worker implements Runnable {

    private final BlockingQueue<Beverage> orderQueue;
    private final Inventory inventory;
    private static final boolean IS_RUNNING = true;

    @Override
    public void run() {
        log.debug("{} started", Thread.currentThread().getName());
        try {
            while (IS_RUNNING) {
                Beverage beverage = orderQueue.take();
                log.debug("Preparing order for beverage {}", beverage);
                InventoryResponse inventoryResponse = inventory.transact(beverage.getIngredientsQty());
                log.debug("InventoryResponse for beverage {}: {}", beverage.getName(), inventoryResponse);
                if (inventoryResponse.getResult() == InventoryResponse.Result.FAILURE) {
                    log.info("{} cannot be prepared because {}", beverage.getName(), inventoryResponse.getMessage());
                } else {
                    if (Constants.BEVERAGE_PREP_TIME_MS > 0) {
                        TimeUnit.MILLISECONDS.sleep(Constants.BEVERAGE_PREP_TIME_MS);
                    }
                    log.info("{} is prepared", beverage.getName());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
