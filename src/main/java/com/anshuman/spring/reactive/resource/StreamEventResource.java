package com.anshuman.spring.reactive.resource;

import com.anshuman.spring.reactive.webclient.SSEWebClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

// An SSE (Stream Server Event) is a specification adopted by most browsers to allow streaming events uni-directionally at any time.
// The ‘events' are just a stream of UTF-8 encoded text data that follow the format defined by the specification.
// This format consists of a series of key-value elements (id, retry, data and event, which indicates the name) separated by line breaks.
@AllArgsConstructor
@Slf4j
@RestController
public class StreamEventResource {

    private final SSEWebClient sseWebClient;

    // To create an SSE streaming endpoint, we'll have to follow the W3C specifications and designate its MIME type as text/event-stream:
    @GetMapping(path = "/produce/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> produceStreamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }

    // To create SSE without MIME type, wrap it in a ServerSentEvent Object. This also allows specifying metadata for the object.
    @GetMapping("/produce/stream-sse")
    public Flux<ServerSentEvent<String>> produceSSE() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }

    @GetMapping("/consume/stream-flux")
    public void consumeStreamFlux()
    {
        sseWebClient.consumeFlux();
    }

    @GetMapping("/consume/stream-sse")
    public void consumeSSE()
    {
        sseWebClient.consumeServerSentEvent();
    }



}
