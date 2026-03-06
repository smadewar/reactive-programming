package com.Reactive_Programming.Spring_boot_Reactive_programming;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    private final Random random = new Random();

    public Mono<Product> saveProduct(Product product) {
        return Mono.fromCallable(() -> productRepo.save(product))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Product> getProductById(int id) {
        return Mono.fromCallable(() -> productRepo.findById(id).orElse(null))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<Product> getAllProducts() {
        return Mono.fromCallable(productRepo::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<Product> getProductsByBrand(String brand) {
        return Mono.fromCallable(() -> productRepo.findByBrandIgnoreCase(brand))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Simulates an unreliable upstream call that sometimes fails or is slow.
     * Used to demonstrate retry and circuit breaker behavior.
     */
    public Flux<Product> getUnreliablePhones() {
        return Mono.fromCallable(() -> {
                    // 60% of the time, throw an error to trigger retry/circuit breaker
                    if (random.nextDouble() < 0.6) {
                        throw new RuntimeException("Simulated upstream failure while fetching phones");
                    }
                    // 20% of the time, respond slowly
                    if (random.nextDouble() < 0.2) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    return productRepo.findAll();
                })
                .delayElement(Duration.ofMillis(100))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Fallback list of phones used when circuit breaker is open or all retries fail.
     */
    public Flux<Product> getFallbackPhones() {
        List<Product> fallback = List.of(
                new Product(1001, "iPhone 15 Pro", "Apple", 1199.0, 10),
                new Product(1002, "Galaxy S24 Ultra", "Samsung", 1299.0, 15),
                new Product(1003, "Pixel 9 Pro", "Google", 999.0, 8)
        );
        return Flux.fromIterable(fallback);
    }
}