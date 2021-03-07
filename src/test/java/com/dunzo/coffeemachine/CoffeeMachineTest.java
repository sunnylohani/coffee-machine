package com.dunzo.coffeemachine;

import com.dunzo.coffeemachine.dao.Inventory;
import com.dunzo.coffeemachine.model.Input;
import com.dunzo.coffeemachine.service.Driver;
import com.dunzo.coffeemachine.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
class CoffeeMachineTest {

    private Inventory inventory;
    private Driver driver;

    @BeforeEach
    void setup() throws IOException {
        Input input = Utils.parseJsonFile("/input-test.json");
        log.debug("Input: {}", input);
        inventory = new Inventory(input.getMachine().getTotalItemsQty());
        driver = new Driver();
        driver.start(input, inventory);
    }

    @Test
    void testCoffeeMachine() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Assertions.assertEquals(0, inventory.getCurrentQuantity("hot_water"));
        Assertions.assertEquals(400, inventory.getCurrentQuantity("hot_milk"));
        Assertions.assertEquals(60, inventory.getCurrentQuantity("ginger_syrup"));
        Assertions.assertEquals(40, inventory.getCurrentQuantity("sugar_syrup"));
        Assertions.assertEquals(40, inventory.getCurrentQuantity("tea_leaves_syrup"));
    }

    @AfterEach
    void teardown() {
        driver.stop();
    }
}
