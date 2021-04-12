package com.anshuman.spring.reactive.functions;

import com.anshuman.spring.reactive.SpringReactiveMySqlApplication;
import com.anshuman.spring.reactive.model.Car;
import com.anshuman.spring.reactive.repository.CarReactiveRepository;
import com.anshuman.spring.reactive.service.CarReactiveService;
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
class CarFunctionalRoutingTest {

    @Autowired
    private CarReactiveService carReactiveService;

    @Autowired
    private CarFunctionalRouting carRouting;

    @MockBean
    private CarReactiveRepository carRepository;

    @Test
    @Order(1)
    public void givenCarId_whenGetCarById_thenCorrectCar() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(carRouting.getCarByIdRoute())
                .build();

        Car car = new Car(1, "Ford", "Mustang", "Red");

        given(carRepository.findById(1)).willReturn(Mono.just(car));

        client.get()
                .uri(uriBuilder -> uriBuilder.path(byId.getPath()).build(1))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Car.class)
                .isEqualTo(car);
    }

    @Test
    @Order(2)
    public void whenGetAllCars_thenCorrectCars() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(carRouting.getAllCarsRoute())
                .build();

        List<Car> cars = Arrays.asList(
                new Car(1,"Ford", "Mustang", "Red"),
                new Car(2,"Ford", "Bronco", "Orange"));

        Flux<Car> carFlux = Flux.fromIterable(cars);
        given(carRepository.findAll()).willReturn(carFlux);

        client.get()
                .uri(all.getPath())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Car.class)
                .isEqualTo(cars);
    }

    @Test
    @Order(3)
    public void whenUpdateCar_thenCarUpdated() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(carRouting.createUpdateCarRoute())
                .build();

        Car car = new Car(1, "Ford", "Mustang", "Red");

        client.post()
                .uri(save.getPath())
                .body(Mono.just(car), Car.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(carRepository).save(car);
    }

    @Test
    @Order(4)
    public void givenCarId_whenGetCarByIdZero_thenForbidden() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(carRouting.getCarByIdRoute())
                .build();

        client.get()
                .uri(byId.getPath() +"0")
                .exchange()
                .expectStatus()
                .isForbidden();
    }
}