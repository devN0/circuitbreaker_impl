package org.example;

public class Driver {
    public static void main(String[] args) throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistry(10, 5, 5);

        Service service1 = new Service("service1", "http://localhost:8080/service1/health");
        Service service2 = new Service("service1", "http://localhost:8080/service2/health");
        serviceRegistry.addService(service1);
        serviceRegistry.addService(service2);

        Foo circuitBreaker = new Foo(serviceRegistry);

        // START THE PERIODIC HEALTH CHECK
        serviceRegistry.startHealthCheck();

        String response1 = circuitBreaker.routeRequest(service1, "GET https://api.productservice.com/get_product?product_id=1");
        String response2 = circuitBreaker.routeRequest(service2, "GET https://api.productservice.com/get_product?product_id=1");
        System.out.println(response1);
        System.out.println(response2);

    }
}