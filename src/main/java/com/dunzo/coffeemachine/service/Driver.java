package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.dao.Inventory;
import com.dunzo.coffeemachine.exception.CoffeeMachineException;
import com.dunzo.coffeemachine.model.Beverage;
import com.dunzo.coffeemachine.model.Input;
import com.dunzo.coffeemachine.util.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class Driver {

    private ExecutorService threadPool;
    private ScheduledExecutorService inventoryIndicatorScheduler;

    public void start(Input input, Inventory inventory) {
        // Instantiate order queue
        BlockingQueue<Beverage> orderQueue = new LinkedBlockingDeque<>(Constants.ORDER_QUEUE_CAPACITY);

        // Start worker threads
        final int N = input.getMachine().getOutlets().getCount();
        threadPool = Executors.newFixedThreadPool(N);
        for (int i=0; i<N; i++) {
            threadPool.submit(new Worker(orderQueue, inventory));
        }

        // Start ingredients quantity indicator thread
        inventoryIndicatorScheduler = Executors.newSingleThreadScheduledExecutor();
        inventoryIndicatorScheduler.scheduleWithFixedDelay(
                new InventoryIndicator(inventory),
                Constants.INGREDIENTS_INDICATOR_INTERVAL_MS,
                Constants.INGREDIENTS_INDICATOR_INTERVAL_MS,
                TimeUnit.MILLISECONDS);

        // Place orders to the order queue via service
        CoffeeMachineService service = new CoffeeMachineServiceImpl(orderQueue);
        input.getMachine().getBeverages().forEach((name, qtyMap) -> {
            try {
                service.placeOrder(new Beverage(name, qtyMap));
            } catch (CoffeeMachineException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void stop() {
        // Clean up
        log.info("Shutting down thread pool");
        threadPool.shutdownNow();
        inventoryIndicatorScheduler.shutdownNow();
    }
}
