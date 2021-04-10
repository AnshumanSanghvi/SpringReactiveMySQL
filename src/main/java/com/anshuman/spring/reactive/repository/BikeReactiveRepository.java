package com.anshuman.spring.reactive.repository;

import com.anshuman.spring.reactive.model.Bike;
import com.anshuman.spring.reactive.model.Car;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

// any data repository that supports non-blocking reactive streams.
public interface BikeReactiveRepository extends ReactiveCrudRepository<Bike, Integer> {

    @Query("SELECT * FROM bike WHERE make = :make")
    Flux<Car> findByMake(String make);

    @Query("SELECT * FROM bike WHERE model = :model")
    Flux<Car> findByModel(String model);
}