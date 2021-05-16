package com.anshuman.spring.reactive.resource;

import com.anshuman.spring.reactive.model.Bike;
import com.anshuman.spring.reactive.service.BikeReactiveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
public class BikeReactiveResource {

    private final BikeReactiveService bikeReactiveService;

    @PostMapping("/bikes/default")
    public Flux<Bike> generateBikes() {
        List<Bike> bikes = Arrays.asList(
                new Bike("Royal Enfield", "Bullet", "Teal"),
                new Bike("Triumph", "Bonneville", "Silver"),
                new Bike("Harley Davidson", "XR750", "Red"),
                new Bike("Harley Davidson", "VRod", "Steel"),
                new Bike("Triumph", "Speed Twin", "Red")
        );

        log.info("Create some bikes and insert them into the database, blocking for up to 2 seconds");
        return bikeReactiveService.saveAll(bikes);
    }
}
