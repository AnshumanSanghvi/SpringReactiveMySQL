package com.anshuman.spring.reactive.service;

import com.anshuman.spring.reactive.model.Bike;
import com.anshuman.spring.reactive.model.Car;
import com.anshuman.spring.reactive.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@AllArgsConstructor
@Service
@Slf4j
public class VehicleReactiveService {

    private final CarReactiveService carReactiveService;
    private final BikeReactiveService bikeReactiveService;

    // Combining two Flux streams of different types using Flux.zip and Vehicle POJO
    public Flux<Vehicle> findAll()
    {
        Flux<Car> cars = carReactiveService.findAll().subscribeOn(Schedulers.boundedElastic());
        Flux<Bike> bikes = bikeReactiveService.findAll().subscribeOn(Schedulers.boundedElastic());

        return Flux.zip(cars, bikes, Vehicle::new)
                .doOnEach(vehicle -> log.debug("vehicle: {}", vehicle))
                .doOnError(error -> log.error("Error occurred in flux while finding Vehicle by Id", error));
    }

}
