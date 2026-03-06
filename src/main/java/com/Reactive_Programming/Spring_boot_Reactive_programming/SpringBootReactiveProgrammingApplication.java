package com.Reactive_Programming.Spring_boot_Reactive_programming;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringBootReactiveProgrammingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactiveProgrammingApplication.class, args);
	}

	/**
	 * Seed the database with some sample phone products so that
	 * the endpoints immediately return data for learning/testing.
	 */
	@Bean
	CommandLineRunner seedPhones(ProductRepo productRepo) {
		return args -> {
			if (productRepo.count() == 0) {
				List<Product> phones = List.of(
						new Product(1, "iPhone 15 Pro", "Apple", 1199.0, 10),
						new Product(2, "iPhone 15", "Apple", 899.0, 20),
						new Product(3, "Galaxy S24 Ultra", "Samsung", 1299.0, 15),
						new Product(4, "Galaxy S24", "Samsung", 999.0, 25),
						new Product(5, "Pixel 9 Pro", "Google", 999.0, 8),
						new Product(6, "Pixel 9", "Google", 799.0, 12)
				);
				productRepo.saveAll(phones);
			}
		};
	}
}
