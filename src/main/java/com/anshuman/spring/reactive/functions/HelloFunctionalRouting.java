package com.anshuman.spring.reactive.functions;

import com.anshuman.spring.reactive.errorHandler.HelloRoutingErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class HelloFunctionalRouting {

    @Bean
    public RouterFunction<ServerResponse> routeHelloPlain()
    {
        return RouterFunctions.route(RequestPredicates.GET("/hello").and(accept(MediaType.TEXT_PLAIN)), HelloRoutingErrorHandler::returnPlain);
    }

    @Bean
    public RouterFunction<ServerResponse> routeHelloReturnOnError()
    {
        return route(GET("/hello/1").and(accept(MediaType.TEXT_PLAIN)), HelloRoutingErrorHandler::returnOnError);
    }

    @Bean
    public RouterFunction<ServerResponse> routeHelloResumeOnErrorWithFallBackValue()
    {
        return route(GET("/hello/2").and(accept(MediaType.TEXT_PLAIN)), HelloRoutingErrorHandler::resumeOnErrorWithFallbackValue);
    }

    @Bean
    public RouterFunction<ServerResponse> routeHelloResumeOnErrorWithAlternativePath()
    {
        return route(GET("/hello/3").and(accept(MediaType.TEXT_PLAIN)), HelloRoutingErrorHandler::resumeOnErrorWithAlternativePath);
    }

    @Bean
    public RouterFunction<ServerResponse> routeHelloResumeOnErrorWithExceptionHandling()
    {
        return route(GET("/hello/4").and(accept(MediaType.TEXT_PLAIN)), HelloRoutingErrorHandler::resumeOnErrorWithExceptionHandling);
    }

    public static Mono<String> sayHello(ServerRequest request) {
        try {
            String name = request.queryParam("name")
                    .orElseThrow(() -> new Exception("name not provided as query param"));
            return Mono.just("Hello, " + name);
        }
        catch (Exception e) {
            return Mono.error(e);
        }
    }

    public static Mono<String> sayHelloFallback() {
        return Mono.just("Hello Stranger");
    }

}
