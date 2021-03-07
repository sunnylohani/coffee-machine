package com.dunzo.coffeemachine.exception;

public class CoffeeMachineException extends Exception {

    public CoffeeMachineException() {
        super();
    }
    public CoffeeMachineException(String message) {
        super(message);
    }

    public CoffeeMachineException(String message, Throwable cause) {
        super(message, cause);
    }
}
