package com.anshuman.spring.reactive.service;

import com.anshuman.spring.reactive.model.Bike;
import com.anshuman.spring.reactive.repository.BikeReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.logging.Level;

@Service
@AllArgsConstructor
@Slf4j
public class BikeReactiveService {

    private final BikeReactiveRepository bikeReactiveRepository;

    public Flux<Bike> saveAll(@NotNull Iterable<Bike> bikes)
    {
        log.trace("Saving bikes={} via repository on thread={}", bikes, Thread.currentThread().getName());
        return bikeReactiveRepository
                .saveAll(bikes)
                .log("saved bike on thread=" + Thread.currentThread().getName(),
                        Level.INFO, SignalType.ON_NEXT);
    }

    public Flux<Bike> findAll()
    {
        log.trace("finding all bikes via repository on thread={}", Thread.currentThread().getName());
        return bikeReactiveRepository
                .findAll()
                .log("found bike on thread=" + Thread.currentThread().getName(),
                        Level.INFO, SignalType.ON_NEXT);
    }
}
