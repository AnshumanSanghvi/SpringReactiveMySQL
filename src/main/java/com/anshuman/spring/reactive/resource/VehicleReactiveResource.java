package com.anshuman.spring.reactive.resource;

import com.anshuman.spring.reactive.model.Vehicle;
import com.anshuman.spring.reactive.service.VehicleReactiveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
public class VehicleReactiveResource
{
    private final VehicleReactiveService vehicleReactiveService;

    @GetMapping(value = "/vehicle/make/{make}")
    public Flux<Vehicle> findByMake(@PathVariable final String make)
    {
        log.debug("Finding vehicles by make={}", make);
        Flux<Vehicle> vehicleFlux = vehicleReactiveService.findByMake(make);

        log.info("completed request for finding vehicles by make={}", make);
        return vehicleFlux;
    }

    @GetMapping("/vehicle/id/{id}")
    public Mono<Vehicle> findById(@PathVariable final int id)
    {
        log.debug("finding vehicle by id={}", id);
        Mono<Vehicle> vehicleMono = vehicleReactiveService.findById(id);
        log.info("completed request for finding vehicle by id={}", id);
        return vehicleMono;
    }

    @GetMapping("/vehicles")
    public Flux<Vehicle> findAll()
    {
        log.debug("finding all vehicles");
        Flux<Vehicle> vehicleFlux = vehicleReactiveService.findAll();
        log.info("completed request for finding all vehicles");

        return vehicleFlux;
    }

    @PostMapping("/vehicles")
    public Flux<Vehicle> saveAll(@RequestBody final List<Vehicle> vehicles)
    {
        log.debug("saving vehicles={}", vehicles);
        Flux<Vehicle> vehicleFlux = vehicleReactiveService.saveAll(vehicles);
        log.info("completed request for saving all vehicles={}", vehicles);
        return vehicleFlux;
    }

    @PostMapping("/vehicle")
    public Mono<Vehicle> save(@RequestBody @NotNull final Vehicle vehicle)
    {
        log.debug("saving vehicle={}", vehicle);
        Mono<Vehicle> vehicleMono = vehicleReactiveService.save(vehicle);
        log.info("completed request for saving vehicle={}", vehicle);
        return vehicleMono;
    }


}
