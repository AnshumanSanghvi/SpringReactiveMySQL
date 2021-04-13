package com.anshuman.spring.reactive.producer;

import reactor.core.publisher.Flux;

import java.util.Optional;

public class FluxEventProducer {

    public static Flux<Integer> produceIntRange(Integer start, Integer end) {
        return Flux.range(Optional.ofNullable(start).orElse(1), Optional.ofNullable(end).orElse(50));
    }

    public static Flux<Integer> produceIntRangeRateLimited(Integer start, Integer end, Integer limitRate) {
        return produceIntRange(start, end)
                .limitRate(Optional.ofNullable(limitRate).orElse(10));
    }

}
