package com.anshuman.spring.reactive.resource;

import com.anshuman.spring.reactive.model.Car;
import com.anshuman.spring.reactive.service.CarReactiveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
public class CarReactiveResource
{
    private final CarReactiveService carReactiveService;

    @GetMapping(value = "/car/make/{make}")
    public Flux<Car> findByMake(@PathVariable final String make)
    {
        log.debug("Finding cars by make={}", make);
        Flux<Car> carFlux = carReactiveService.findByMake(make);

        log.info("completed request for finding cars by make={}", make);
        return carFlux;
    }

    @GetMapping("/car/id/{id}")
    public Mono<Car> findById(@PathVariable final int id)
    {
        log.debug("finding car by id={}", id);
        Mono<Car> carMono = carReactiveService.findById(id);
        log.info("completed request for finding car by id={}", id);
        return carMono;
    }

    @GetMapping("/cars")
    public Flux<Car> findAll()
    {
        log.debug("finding all cars");
        Flux<Car> carFlux = carReactiveService.findAll();
        log.info("completed request for finding all cars");

        return carFlux;
    }

    @PostMapping("/cars")
    public Flux<Car> saveAll(@RequestBody final List<Car> cars)
    {
        log.debug("saving cars={}", cars);
        Flux<Car> carFlux = carReactiveService.saveAll(cars);
        log.info("completed request for saving all cars={}", cars);
        return carFlux;
    }

    @PostMapping("/car")
    public Mono<Car> save(@RequestBody @NotNull final Car car)
    {
        log.debug("saving car={}", car);
        Mono<Car> carMono = carReactiveService.save(car);
        log.info("completed request for saving car={}", car);
        return carMono;
    }

    @GetMapping("/car")
    public Flux<Car> getByMakeOrModel(@RequestParam("make") String make, @RequestParam("model") String model)
    {
        return carReactiveService.findByMakeOrModel(make, model);
    }


}
