package com.Reactive_Programming.Spring_boot_Reactive_programming.producer;


import com.Reactive_Programming.Spring_boot_Reactive_programming.Product;
import com.Reactive_Programming.Spring_boot_Reactive_programming.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
     * Save a list of phones in one request. IDs can be omitted for new products (auto-generated).
     */
    @PostMapping("/save")
    public Mono<ResponseEntity<List<Product>>> saveProduct(@RequestBody List<Product> products) {
        return Flux.fromIterable(products)
                .flatMap(productService::saveProduct)
                .collectList()
                .map(ResponseEntity::ok);
    }

    /**
     * Save multiple phones in one request (bulk create/update).
     * This lets you post an array of phones from your client.
     */
    @PostMapping("/bulk")
    public Flux<Product> saveProducts(@RequestBody Flux<Product> products) {
        return products.flatMap(productService::saveProduct);
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

    /**
     * Same unreliable source, but using pure Reactor error handling instead
     * of Resilience4j: demonstrates onErrorResume / onErrorReturn style.
     */
    @GetMapping("/unreliable-onerror")
    public Flux<Product> getUnreliablePhonesOnError() {
        return productService.getUnreliablePhones()
                .onErrorResume(ex -> productService.getFallbackPhones());
    }
}
