package com.anshuman.spring.reactive.functions;


import com.anshuman.spring.reactive.model.Vehicle;
import com.anshuman.spring.reactive.repository.VehicleReactiveRepository;
import com.anshuman.spring.reactive.filters.ProtectedIdHandlerFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static com.anshuman.spring.reactive.Constants.Endpoint.all;
import static com.anshuman.spring.reactive.Constants.Endpoint.byIdVar;
import static com.anshuman.spring.reactive.Constants.Endpoint.byMakeVar;
import static com.anshuman.spring.reactive.Constants.Endpoint.save;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// Creates routes using RouterFunction to publish and consume our reactive streams of Vehicles.
@Configuration
@AllArgsConstructor
public class VehicleFunctionalRouting {

    private final VehicleReactiveRepository vehicleRepository;

    // RouterFunction that publishes a single Vehicle resource:
    @Bean
    RouterFunction<ServerResponse> getVehicleByIdRoute() {
        // The first argument is a request predicate. We use a statically imported RequestPredicates.GET method here.
        // The second parameter defines a handler function that'll be used if the predicate applies.
        return route(GET(byIdVar.getPath()),
                req -> ok().body(
                        vehicleRepository.findById(Integer.parseInt(req.pathVariable("id"))), Vehicle.class))
                // adding a custom filter to handler functions.
                .filter(new ProtectedIdHandlerFilter());
    }

    // RouterFunction that publishes a Vehicle collection resource:
    @Bean
    RouterFunction<ServerResponse> getAllVehiclesRoute() {

        return route(GET(all.getPath()),
                req -> ok().body(
                        vehicleRepository.findAll(), Vehicle.class));
    }

    @Bean
    RouterFunction<ServerResponse> getVehiclesByMakeRoute() {

        return route(GET(byMakeVar.getPath()),
                req -> {
                    Flux<Vehicle> vehicleFlux = vehicleRepository.findByMake(req.pathVariable("make"));
                    // return http status 404 not found using Flux.hasElements() method to check if the flux has data.
                    return vehicleFlux.hasElements()
                            .flatMap(hasElements -> hasElements ? ok().body(vehicleFlux, Vehicle.class) : notFound().build());
                }
        );
    }

    @Bean
    RouterFunction<ServerResponse> updateVehicleRoute() {
        return route(POST(save.getPath()),
                req -> req.body(toMono(Vehicle.class))
                        .doOnNext(vehicleRepository::save)
                        .then(ok().build()));
    }

    // We can also compose the routes together in a single router function.
    // RouterFunction.and() combines our routes.
    @Bean
    RouterFunction<ServerResponse> composedRoutes() {
        return
                route(GET(all.getPath()),
                        req -> ok().body(
                                vehicleRepository.findAll(), Vehicle.class))

                        .and(route(GET(byIdVar.getPath()),
                                req -> ok().body(
                                        vehicleRepository.findById(Integer.parseInt(req.pathVariable("id"))), Vehicle.class)))

                        .and(route(POST(save.getPath()),
                                req -> req.body(toMono(Vehicle.class))
                                        .doOnNext(vehicleRepository::save)
                                        .then(ok().build())));
    }

}

