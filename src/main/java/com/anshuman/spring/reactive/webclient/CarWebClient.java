package com.anshuman.spring.reactive.webclient;

import com.anshuman.spring.reactive.model.Car;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static com.anshuman.spring.reactive.Constants.Endpoint.all;
import static com.anshuman.spring.reactive.Constants.Endpoint.byIdVar;

//We can use CarWebClient to create a client to retrieve data from the endpoints provided by the CarReactiveResource.
@AllArgsConstructor
@Component
@Slf4j
public class CarWebClient {

    // Can also simply create webclient like this instead of bean.
    // private final WebClient client = WebClient.create("http://localhost:8080");
    private final WebClient client;

    public void getById(int carId) {
        // To retrieve single resource of type Mono from endpoint
        client.get()
                .uri(byIdVar.getPath(), "1")
                .retrieve()
                .bodyToMono(Car.class)
                .subscribe(car -> log.info("car={}", car),
                throwable -> log.error("exception encountered when processing carFlux", throwable));
    }

    public void getAllCars() {
        // to retrieve a collection resource of type Flux from endpoint
        Flux<Car> carFlux = client.get()
                .uri(all.getPath())
                .retrieve()
                .bodyToFlux(Car.class);

        // simply log each element in the Flux stream
        carFlux.log()
                // the checkpoint method is a way to debug the output of a stream and get the optional stack trace
                .checkpoint("Observed error on carFlux", true)
                // the subscribe method provides another argument where you can handle a throwable exception
                .subscribe(car -> log.info("car={}", car),
                        throwable -> log.error("exception encountered when processing carFlux", throwable));
    }

    /**
     * Retries
     **/

    public Mono<Car> getbyIdRetryTimes(int times) {
        return client.get()
                .uri(byIdVar.getPath(), "1")
                .retrieve()
                .bodyToMono(Car.class)
                .retry(times);
    }

    public void getByIdRetryObject(int times, int durationSeconds) {

        // use a custom retry object
        Retry retry = Retry
                // retry with custom attempts and backing off with custom duration
                .backoff(times, Duration.ofSeconds(durationSeconds))
                // apply retry strategy only in case of specific exceptions
                .filter(throwable -> throwable instanceof ResponseStatusException)
                // custom exception handling insted of throwing retry exhausted exception
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service failed to process after max retries");
                });

        client.get()
                .uri(byIdVar.getPath(), "1")
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error")))
                .bodyToMono(String.class)
                .retryWhen(retry);
    }


}
