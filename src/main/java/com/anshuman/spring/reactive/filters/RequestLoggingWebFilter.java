package com.anshuman.spring.reactive.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class RequestLoggingWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        log.debug("Request: path={}, params={}, remoteAddress={}", request.getPath().toString(), request.getQueryParams(),
                Optional.ofNullable(request.getRemoteAddress()).map(String::valueOf).orElse(""));
        return webFilterChain.filter(serverWebExchange);
    }

}
