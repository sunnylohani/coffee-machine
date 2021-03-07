package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.dao.Inventory;
import com.dunzo.coffeemachine.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class InventoryIndicator implements Runnable {

    private final Inventory inventory;

    @Override
    public void run() {
        List<String> items = inventory.getLowRunningItems();
        if (!items.isEmpty()) {
            log.warn(String.format("Following ingredients running low below threshold (%s): %s",
                    Constants.INGREDIENTS_LOW_THRESHOLD,
                    String.join(",", items)));
            log.info("Enter 'r' in 5 seconds to refill: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            long startTime = Instant.now().toEpochMilli();
            try {
                while ((Instant.now().toEpochMilli() - startTime) < 5000 && !br.ready()) {
                    // Noop
                }
                if (br.ready()) {
                    String input = br.readLine();
                    if ("r".equalsIgnoreCase(input)) {
                        items.forEach(inventory::refillItem);
                    } else {
                        log.error("Invalid user input!");
                    }
                } else {
                    log.warn("Did not receive user input. Refill not done.");
                }
            } catch (IOException e) {
                log.error("Exception occurred while reading user input", e);
            }
        }
    }
}
