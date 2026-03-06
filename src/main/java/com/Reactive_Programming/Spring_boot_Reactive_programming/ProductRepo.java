package com.Reactive_Programming.Spring_boot_Reactive_programming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByBrandIgnoreCase(String brand);
}