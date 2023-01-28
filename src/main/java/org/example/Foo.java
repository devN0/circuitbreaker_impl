package org.example;

import lombok.Data;
import org.example.exceptions.IllegalCircuitBreakerStateException;

@Data
public class Foo { // circuit breaker impl
    private ServiceRegistry serviceRegistry;

    public Foo(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public String routeRequest(Service service, String request) throws Exception {
        State state = serviceRegistry.getServiceStateMap().get(service);
        return switch (state) {
            case CLOSED -> handleRequest(service, request);
            case OPEN -> getFallBack(service);
            case PARTIALLY_OPEN -> "Service is currently in recovery. Please wait while we resolve the issue.";
            default -> throw new IllegalCircuitBreakerStateException(state.name());
        };
    }

    private String handleRequest(Service service, String request) throws Exception {
        return service.handleRequest(request);
    }

    private String getFallBack(Service service) {
        return "Service: "+ service + " is down at the moment. Please try again later";
    }
}
