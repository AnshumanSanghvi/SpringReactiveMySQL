package com.anshuman.spring.reactive.service;

import com.anshuman.spring.reactive.model.Car;
import com.anshuman.spring.reactive.repository.CarReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.util.logging.Level;

@Service
@AllArgsConstructor
@Slf4j
public class CarReactiveService
{

    private final CarReactiveRepository carReactiveRepository;

    public Mono<Car> save(Car car)
    {
        log.trace("Saving car={} via repository on thread={}", car, Thread.currentThread().getName());
        return carReactiveRepository
                .save(car)
                // log each element
                .log("saved car", Level.INFO, SignalType.ON_NEXT);
    }

    public Flux<Car> saveAll(Iterable<Car> cars)
    {
        log.trace("Saving cars={} via repository on thread={}", cars, Thread.currentThread().getName());
        return carReactiveRepository
                .saveAll(cars)
                .log("saved car on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT)
                .doOnError(error -> log.error("Error occurred in flux while saving Car", error));
    }

    public Flux<Car> findByMake(String make)
    {
        log.trace("finding cars by make={} via repository on thread={}", make, Thread.currentThread().getName());
        return carReactiveRepository
                .findByMake(make)
                .log("found car by make on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT)
                .doOnError(error -> log.error("Error occurred in flux while finding Car by make", error));
    }

    // We wrap a single Car resource in a Mono because we return at most one employee.
    public Mono<Car> findById(int id)
    {
        log.trace("finding car by id={} via repository on thread={}", id, Thread.currentThread().getName());
        return carReactiveRepository.findById(id).log("found car by id on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT)
                .doOnError(error -> log.error("Error occurred in flux while finding Car by Id", error));
    }

    // For the collection resource, we use a Flux of type Car – since that's the publisher for 0..n elements.
    public Flux<Car> findAll()
    {
        log.trace("finding all cars via repository on thread={}", Thread.currentThread().getName());
        return carReactiveRepository
                .findAll()
                .log("found car on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT)
                .doOnError(error -> log.error("Error occurred in flux while finding all Cars", error));
    }

    public Flux<Car> findByModel(String model)
    {
        log.trace("finding car by model={} via repository on thread={}", model, Thread.currentThread().getName());
        return carReactiveRepository.findByModel(model)
                .log("found car on thread=" + Thread.currentThread().getName(),
                Level.INFO, SignalType.ON_NEXT)
                .doOnError(error -> log.error("Error occurred in flux while finding Car by model", error));
    }


    // Making parallel calls to two Flux<Car> Streams using Flux.merge and Flux.parallel
    public Flux<Car> findByMakeOrModel(String make, String model)
    {
        return Flux.merge(findByMake(make), findByModel(model))
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .ordered((v1, v2) -> v2.getId() - v1.getId())
                .doOnError(error -> log.error("Error occurred in flux while finding Car by make or model", error));
    }


}
