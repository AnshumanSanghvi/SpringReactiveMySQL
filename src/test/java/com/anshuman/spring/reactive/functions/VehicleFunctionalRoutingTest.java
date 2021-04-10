package com.anshuman.spring.reactive.functions;

import com.anshuman.spring.reactive.SpringReactiveMySqlApplication;
import com.anshuman.spring.reactive.model.Vehicle;
import com.anshuman.spring.reactive.repository.VehicleReactiveRepository;
import com.anshuman.spring.reactive.service.VehicleReactiveService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static com.anshuman.spring.reactive.Constants.Endpoint.all;
import static com.anshuman.spring.reactive.Constants.Endpoint.byId;
import static com.anshuman.spring.reactive.Constants.Endpoint.save;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringReactiveMySqlApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled // to run the tests, remove the command line runner bean from main App.
class VehicleFunctionalRoutingTest {

    @Autowired
    private VehicleReactiveService vehicleReactiveService;

    @Autowired
    private VehicleFunctionalRouting vehicleRouting;

    @MockBean
    private VehicleReactiveRepository vehicleRepository;

    @Test
    @Order(1)
    public void givenVehicleId_whenGetVehicleById_thenCorrectVehicle() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(vehicleRouting.getVehicleByIdRoute())
                .build();

        Vehicle vehicle = new Vehicle(1, "Ford", "Mustang", "Red");

        given(vehicleRepository.findById(1)).willReturn(Mono.just(vehicle));

        client.get()
                .uri(uriBuilder -> uriBuilder.path(byId.getPath()).build(1))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vehicle.class)
                .isEqualTo(vehicle);
    }

    @Test
    @Order(2)
    public void whenGetAllVehicles_thenCorrectVehicles() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(vehicleRouting.getAllVehiclesRoute())
                .build();

        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle(1,"Ford", "Mustang", "Red"),
                new Vehicle(2,"Ford", "Bronco", "Orange"));

        Flux<Vehicle> vehicleFlux = Flux.fromIterable(vehicles);
        given(vehicleRepository.findAll()).willReturn(vehicleFlux);

        client.get()
                .uri(all.getPath())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Vehicle.class)
                .isEqualTo(vehicles);
    }

    @Test
    @Order(3)
    public void whenUpdateVehicle_thenVehicleUpdated() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(vehicleRouting.updateVehicleRoute())
                .build();

        Vehicle vehicle = new Vehicle(1, "Ford", "Mustang", "Red");

        client.post()
                .uri(save.getPath())
                .body(Mono.just(vehicle), Vehicle.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    @Order(4)
    public void givenVehicleId_whenGetVehicleByIdZero_thenForbidden() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(vehicleRouting.getVehicleByIdRoute())
                .build();

        client.get()
                .uri(byId.getPath() +"0")
                .exchange()
                .expectStatus()
                .isForbidden();
    }
}