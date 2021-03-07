package com.dunzo.coffeemachine;

import com.dunzo.coffeemachine.dao.Inventory;
import com.dunzo.coffeemachine.model.Input;
import com.dunzo.coffeemachine.service.Driver;
import com.dunzo.coffeemachine.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CoffeeMachine {
    public static void main(String[] args) throws IOException {
        Input input = Utils.parseJsonFile("/input.json");
        log.debug("Input: {}", input);

        // Init inventory
        Inventory inventory = new Inventory(input.getMachine().getTotalItemsQty());

        // Create and start driver
        Driver driver = new Driver();
        driver.start(input, inventory);

        Thread shutdownHook = new Thread(driver::stop);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
