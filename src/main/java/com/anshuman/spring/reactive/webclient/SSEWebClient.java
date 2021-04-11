package com.anshuman.spring.reactive.webclient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;

@AllArgsConstructor
@Component
@Slf4j
public class SSEWebClient {

    private final WebClient client;

    @Async
    public void consumeFlux() {
        Flux<String> stringStream = client
                .get()
                .uri("/produce/stream-flux")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        stringStream.subscribe(
                content -> log.info("Current time: {} - Received content: {} ", LocalTime.now(), content),
                error -> log.error("Error retrieving content: {}", error.getMessage(), error),
                () -> log.info("Completed!!!"));
    }

    @Async
    public void consumeServerSentEvent() {

        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {};

        Flux<ServerSentEvent<String>> eventStream = client
                .get()
                .uri("/produce/stream-sse")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> log.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> log.error("Error receiving SSE: {}", error.getMessage(), error),
                () -> log.info("Completed!!!"));
    }
}
