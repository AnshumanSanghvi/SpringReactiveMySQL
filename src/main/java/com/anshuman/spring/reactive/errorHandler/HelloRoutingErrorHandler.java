package com.anshuman.spring.reactive.errorHandler;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.anshuman.spring.reactive.functions.HelloFunctionalRouting.sayHello;
import static com.anshuman.spring.reactive.functions.HelloFunctionalRouting.sayHelloFallback;

// Class to handle routing function response with error handling
public class HelloRoutingErrorHandler {

    public static @NotNull Mono<ServerResponse> returnPlain(ServerRequest request) {
        return Mono.just("Hello, " + request.queryParam("name").get())
                .flatMap(s -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(s));
    }

    // example of handling errors with "onErrorReturn" method for Mono and Flux
    public static @NotNull Mono<ServerResponse> returnOnError(ServerRequest request) {
        return sayHello(request)
                .onErrorReturn("Hello, Stranger")
                .flatMap(s -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(s));
    }

    // example of handling errors with "onErrorResume" method for Mono and Flux
    public static @NotNull Mono<ServerResponse> resumeOnErrorWithFallbackValue(ServerRequest request) {
        return sayHello(request)
                .flatMap(s -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(s))
                .onErrorResume(e -> Mono.just("Error " + e.getMessage())
                        .flatMap(s -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(s)));
    }

    // example of handling errors with "onErrorResume" method for Mono and Flux
    public static @NotNull Mono<ServerResponse> resumeOnErrorWithAlternativePath(ServerRequest request) {
        return sayHello(request)
                .flatMap(s -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(s))
                .onErrorResume(e -> sayHelloFallback()
                        .flatMap(s -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(s)));
    }

    // example of handling errors with "onErrorResume" method for Mono and Flux
    public static @NotNull Mono<ServerResponse> resumeOnErrorWithExceptionHandling(ServerRequest request) {
        return ServerResponse.ok()
                .body(sayHello(request)
                                .onErrorResume(e -> Mono.error(new NameRequiredException(HttpStatus.BAD_REQUEST, "username is required", e))),
                        String.class);
    }

    static class NameRequiredException extends ResponseStatusException {
        public NameRequiredException(HttpStatus status, String message, Throwable e) {
            super(status, message, e);
        }
    }
}
