package com.Reactive_Programming.Spring_boot_Reactive_programming.producer;


import com.Reactive_Programming.Spring_boot_Reactive_programming.Product;
import com.Reactive_Programming.Spring_boot_Reactive_programming.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/phones")
public class producerController {

    @Autowired
    private ProductService productService;

    /**
     * Basic CRUD-style reactive endpoint: get phone by id.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable int id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Save a phone (create/update) in a reactive way.
     */
    @PostMapping
    public Mono<ResponseEntity<Product>> saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product)
                .map(ResponseEntity::ok);
    }

    /**
     * Get all phones as a reactive stream (Flux).
     */
    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Filter phones by brand/company (Apple, Samsung, etc).
     */
    @GetMapping("/brand/{brand}")
    public Flux<Product> getProductsByBrand(@PathVariable String brand) {
        return productService.getProductsByBrand(brand);
    }

    /**
     * Endpoint that demonstrates reactive + Resilience4j:
     * - retry when failures happen
     * - circuit breaker to stop hammering a failing upstream
     * - fallback when everything fails
     */
    @GetMapping("/unreliable")
    @Retry(name = "phoneService")
    @CircuitBreaker(name = "phoneService", fallbackMethod = "fallbackPhones")
    public Flux<Product> getUnreliablePhones() {
        return productService.getUnreliablePhones();
    }

    /**
     * Fallback method for circuit breaker / retry.
     * Signature must match original method + Throwable at the end.
     */
    public Flux<Product> fallbackPhones(Throwable throwable) {
        return productService.getFallbackPhones();
    }
}
