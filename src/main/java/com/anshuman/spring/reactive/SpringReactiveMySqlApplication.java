package com.anshuman.spring.reactive;

import com.anshuman.spring.reactive.model.Bike;
import com.anshuman.spring.reactive.model.Car;
import com.anshuman.spring.reactive.service.BikeReactiveService;
import com.anshuman.spring.reactive.service.CarReactiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
@EnableAsync
public class SpringReactiveMySqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveMySqlApplication.class, args);
	}

	@Bean
	public CommandLineRunner reactiveDatabaseExample(CarReactiveService carReactiveService, BikeReactiveService bikeReactiveService) {
		return args -> {

			generateCars(carReactiveService);

			generateBikes(bikeReactiveService);

		};
	}

	private void generateBikes(BikeReactiveService bikeReactiveService) {
		List<Bike> bikes = Arrays.asList(
				new Bike("Royal Enfield", "Bullet", "Teal"),
				new Bike("Triumph", "Bonneville", "Silver"),
				new Bike("Harley Davidson", "XR750", "Red"),
				new Bike("Harley Davidson", "VRod", "Steel"),
				new Bike("Triumph", "Speed Twin", "Red")
		);

		System.out.println("Create some bikes and insert them into the database, blocking for up to 2 seconds");
		bikeReactiveService.saveAll(bikes)
				.blockLast(Duration.ofSeconds(2L));
	}

	private void generateCars(CarReactiveService carReactiveService) {
		List<Car> cars = Arrays.asList(
				new Car("Ford", "Mustang", "Red"),
				new Car("Ford", "Bronco", "Orange"),
				new Car("Chevy", "Silverado", "Blue"),
				new Car("Chevy", "Tahoe", "Black"),
				new Car("Toyota", "Supra", "Green")
		);

		System.out.println("Create some cars and insert them into the database, blocking for up to 2 seconds");
		carReactiveService.saveAll(cars)
				.blockLast(Duration.ofSeconds(2L));
	}


}
