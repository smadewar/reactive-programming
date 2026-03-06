package com.Reactive_Programming.Spring_boot_Reactive_programming;

import com.Reactive_Programming.Spring_boot_Reactive_programming.Product;
import com.Reactive_Programming.Spring_boot_Reactive_programming.ProductRepo;
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
}
