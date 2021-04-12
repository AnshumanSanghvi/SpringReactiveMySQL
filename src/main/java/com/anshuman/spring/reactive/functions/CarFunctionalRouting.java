package com.anshuman.spring.reactive.functions;


import com.anshuman.spring.reactive.filters.ProtectedIdHandlerFilter;
import com.anshuman.spring.reactive.model.Car;
import com.anshuman.spring.reactive.repository.CarReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.Optional;

import static com.anshuman.spring.reactive.Constants.Endpoint.all;
import static com.anshuman.spring.reactive.Constants.Endpoint.byIdVar;
import static com.anshuman.spring.reactive.Constants.Endpoint.byMakeVar;
import static com.anshuman.spring.reactive.Constants.Endpoint.save;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// Creates routes using RouterFunction to publish and consume our reactive streams of Cars.
@Configuration
@AllArgsConstructor
@Slf4j
public class CarFunctionalRouting {

    private final CarReactiveRepository carRepository;

    // RouterFunction that publishes a single Car resource:
    @Bean
    RouterFunction<ServerResponse> getCarByIdRoute() {
        // The first argument is a request predicate. We use a statically imported RequestPredicates.GET method here.
        // The second parameter defines a handler function that'll be used if the predicate applies.
        return route(GET(byIdVar.getPath()),
                req -> ok().body(
                        carRepository.findById(Integer.parseInt(req.pathVariable("id"))), Car.class))
                // adding a custom filter to handler functions.
                .filter(new ProtectedIdHandlerFilter());
    }

    // RouterFunction that publishes a Car collection resource:
    @Bean
    RouterFunction<ServerResponse> getAllCarsRoute() {

        return route(GET(all.getPath()),
                req -> ok().body(
                        carRepository.findAll(), Car.class));
    }

    @Bean
    RouterFunction<ServerResponse> getCarsByMakeRoute() {

        return route(GET(byMakeVar.getPath()),
                req -> {
                    String make = req.pathVariable("make");
                    Flux<Car> carFlux = carRepository.findByMake(make);
                    return carFlux
                            // return http status 404 not found using Flux.hasElements() method to check if the flux has data.
                            .hasElements()
                            .doOnEach(hasElements ->
                                    log.trace("{} found for make={}",
                                            Optional.ofNullable(hasElements.get()).orElse(false) ? "Cars" : "No Cars",
                                            make))
                            .flatMap(hasElements -> hasElements ? ok().body(carFlux, Car.class) : notFound().build());
                }
        );
    }

    @Bean
    RouterFunction<ServerResponse> createUpdateCarRoute() {
        return route(POST(save.getPath()),
                req -> req.bodyToMono(Car.class)
                        .flatMap(car -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(carRepository.save(car), Car.class)));
    }


    // We can also compose the routes together in a single router function.
    // RouterFunction.and() combines our routes.
    @Bean
    RouterFunction<ServerResponse> composedRoutes() {
        return
                route(GET(all.getPath()),
                        req -> ok().body(
                                carRepository.findAll(), Car.class))

                        .and(route(GET(byIdVar.getPath()),
                                req -> ok().body(
                                        carRepository.findById(Integer.parseInt(req.pathVariable("id"))), Car.class)));
    }

}

