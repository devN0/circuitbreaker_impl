package org.example.exceptions;

public class IllegalCircuitBreakerStateException extends IllegalArgumentException {
    public IllegalCircuitBreakerStateException(String state) {
        super("State: "+ state + " not recognized.");
    }
}
