package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceRegistry {
    private final Map<Service, State> serviceStateMap;
    private final Map<Service, Integer> failedRequests;
    private final Map<Service, Integer> successfulRequests;
    private final int maxAllowedRequests;
    private final int failureThreshold;
    private final int recoveryThreshold;
    private final ScheduledExecutorService scheduledExecutorService;

    public ServiceRegistry(int maxAllowedRequests, int failureThreshold, int recoveryThreshold) {
        serviceStateMap = new ConcurrentHashMap<>();
        failedRequests = new ConcurrentHashMap<>();
        successfulRequests = new ConcurrentHashMap<>();
        this.maxAllowedRequests = maxAllowedRequests;
        this.failureThreshold = failureThreshold;
        this.recoveryThreshold = recoveryThreshold;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public Map<Service, State> getServiceStateMap() {
        return serviceStateMap;
    }

    public Map<Service, Integer> getFailedRequests() {
        return failedRequests;
    }

    public Map<Service, Integer> getSuccessfulRequests() {
        return successfulRequests;
    }

    public int getMaxAllowedRequests() {
        return maxAllowedRequests;
    }

    public int getFailureThreshold() {
        return failureThreshold;
    }

    public int getRecoveryThreshold() {
        return recoveryThreshold;
    }

    public void registerService(Service service) {
        serviceStateMap.put(service, State.CLOSED);
        successfulRequests.put(service, 0);
    }

    public void startHealthCheck() {
        scheduledExecutorService.scheduleAtFixedRate(this::checkHealth, 0, 120, TimeUnit.SECONDS);
    }

    private void checkHealth() {
        for(Map.Entry<Service, State> entry : serviceStateMap.entrySet()) {
            Service service = entry.getKey();
            checkServiceReliability(service);

            if(successfulRequests.get(service) >= recoveryThreshold) {
                serviceStateMap.put(service, State.CLOSED);
            }
            if(failedRequests.get(service) > failureThreshold) {
                serviceStateMap.put(service, State.OPEN);
            }

            // Reset request maps
            successfulRequests.put(service, 0);
            failedRequests.put(service, 0);
        }
    }

    private void checkServiceReliability(Service service) {
        int currentRequestCount = 0;
        while(currentRequestCount < maxAllowedRequests) {
            boolean isHealthy = service.healthCheck();
            if(isHealthy) { // Service is healthy
                successfulRequests.put(service, successfulRequests.get(service)+1);
            } else { // Service is unhealthy
                failedRequests.put(service, failedRequests.get(service)+1);
            }
            currentRequestCount++;
        }
    }
}
