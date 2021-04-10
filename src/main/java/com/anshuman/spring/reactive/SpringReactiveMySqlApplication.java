package com.anshuman.spring.reactive;

import com.anshuman.spring.reactive.model.Vehicle;
import com.anshuman.spring.reactive.service.VehicleReactiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
public class SpringReactiveMySqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveMySqlApplication.class, args);
	}

	@Bean
	public CommandLineRunner reactiveDatabaseExample(VehicleReactiveService vehicleReactiveService) {
		return args -> {

			List<Vehicle> vehicles = Arrays.asList(
					new Vehicle("Ford", "Mustang", "Red"),
					new Vehicle("Ford", "Bronco", "Orange"),
					new Vehicle("Chevy", "Silverado", "Blue"),
					new Vehicle("Chevy", "Tahoe", "Black"),
					new Vehicle("Toyota", "Supra", "Green")
			);

			System.out.println("Create some vehicles and insert them into the database, blocking for up to 5 seconds");
			vehicleReactiveService
					.saveAll(vehicles)
					.blockLast(Duration.ofSeconds(5));

		};
	}


}
