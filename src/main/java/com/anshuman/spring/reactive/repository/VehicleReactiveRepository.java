package com.anshuman.spring.reactive.repository;

import com.anshuman.spring.reactive.model.Vehicle;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

// any data repository that supports non-blocking reactive streams.
public interface VehicleReactiveRepository extends ReactiveCrudRepository<Vehicle, Integer> {

    @Query("SELECT * FROM vehicle WHERE make = :make")
    Flux<Vehicle> findByMake(String make);

    @Query("SELECT * FROM vehicle WHERE model = :model")
    Flux<Vehicle> findByModel(String model);
}