package com.anshuman.spring.reactive.service;

import com.anshuman.spring.reactive.model.Vehicle;
import com.anshuman.spring.reactive.repository.VehicleReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.logging.Level;

@Service
@AllArgsConstructor
@Slf4j
public class VehicleReactiveService
{
    @Autowired
    private final VehicleReactiveRepository vehicleReactiveRepository;

    public Mono<Vehicle> save(@NotNull Vehicle vehicle)
    {
        log.trace("Saving vehicle={} via repository on thread={}", vehicle, Thread.currentThread().getName());
        return vehicleReactiveRepository
                .save(vehicle)
                .log("saved vehicle", Level.INFO, SignalType.ON_NEXT);
    }

    public Flux<Vehicle> saveAll(@NotNull Iterable<Vehicle> vehicles)
    {
        log.trace("Saving vehicles={} via repository on thread={}", vehicles, Thread.currentThread().getName());
        return vehicleReactiveRepository
                .saveAll(vehicles)
                .log("saved vehicle on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT);
    }

    public Flux<Vehicle> findByMake(String make)
    {
        log.trace("finding vehicles by make={} via repository on thread={}", make, Thread.currentThread().getName());
        return vehicleReactiveRepository
                .findByMake(make)
                .log("found vehicle by make on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT);
    }

    // We wrap a single Vehicle resource in a Mono because we return at most one employee.
    public Mono<Vehicle> findById(int id)
    {
        log.trace("finding vehicle by id={} via repository on thread={}", id, Thread.currentThread().getName());
        return vehicleReactiveRepository.findById(id).log("found vehicle by id on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT);
    }

    // For the collection resource, we use a Flux of type Vehicle – since that's the publisher for 0..n elements.
    public Flux<Vehicle> findAll()
    {
        log.trace("finding all vehicles via repository on thread={}", Thread.currentThread().getName());
        return vehicleReactiveRepository
                .findAll()
                .log("found vehicle on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT);
    }

}
