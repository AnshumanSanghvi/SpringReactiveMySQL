package com.anshuman.spring.reactive.resource;

import com.anshuman.spring.reactive.model.Vehicle;
import com.anshuman.spring.reactive.service.VehicleReactiveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Slf4j
@RestController
public class VehicleReactiveResource {

    private final VehicleReactiveService vehicleReactiveService;

    @GetMapping("/vehicles")
    public Flux<Vehicle> findAll()
    {
        log.debug("finding all vehicles");
        Flux<Vehicle> vehicleFlux = vehicleReactiveService.findAll();
        log.info("completed request for finding all vehicles");
        return vehicleFlux;
    }
}
