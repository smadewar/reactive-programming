package com.Reactive_Programming.Spring_boot_Reactive_programming;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id; // nullable so we can create Product without supplying id
    private String name;   // e.g. model: "iPhone 15 Pro"
    private String brand;  // e.g. company: "Apple", "Samsung"
    private double price;
    private int quantity;

    // No-args constructor
    public Product() {
    }

    // All-args constructor
    public Product(Integer id, String name, String brand, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
    }

    // Constructor without id for creating new entities before persisting
    public Product(String name, String brand, double price, int quantity) {
        this(null, name, brand, price, quantity);
    }



}
