package com.anshuman.spring.reactive.webclient;

import com.anshuman.spring.reactive.model.Vehicle;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.anshuman.spring.reactive.Constants.Endpoint.all;
import static com.anshuman.spring.reactive.Constants.Endpoint.byIdVar;

//We can use VehicleWebClient to create a client to retrieve data from the endpoints provided by the VehicleReactiveResource.
@AllArgsConstructor
@Component
public class VehicleWebClient {

    // Can also simply create webclient like this instead of bean.
    // private final WebClient client = WebClient.create("http://localhost:8080");
    private final WebClient client;

    public void getById(int vehicleId)
    {
        // To retrieve single resource of type Mono from endpoint
        Mono<Vehicle> vehicleMono = client.get()
                .uri(byIdVar.getPath(), "1")
                .retrieve()
                .bodyToMono(Vehicle.class);

        vehicleMono.subscribe(System.out::println);
    }

    public void getAllVehicles()
    {
        // to retrieve a collection resource of type Flux from endpoint
        Flux<Vehicle> vehicleFlux = client.get()
                .uri(all.getPath())
                .retrieve()
                .bodyToFlux(Vehicle.class);

        vehicleFlux.subscribe(System.out::println);
    }


}
