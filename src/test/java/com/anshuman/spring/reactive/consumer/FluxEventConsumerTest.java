package com.anshuman.spring.reactive.consumer;

import com.anshuman.spring.reactive.producer.FluxEventProducer;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxEventConsumerTest {

    @Test
    public void consumeIntRange() {
        Flux<Integer> integerFlux = FluxEventConsumer.fluxIntRangeConsumer(FluxEventProducer.produceIntRange(1, 50));

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenRequest(10)
                .thenRequest(10)
                .expectNext(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
                .thenRequest(10)
                .expectNext(21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
                .thenRequest(10)
                .expectNext(31, 32, 33, 34, 35, 36, 37, 38, 39, 40)
                .thenRequest(10)
                .expectNext(41, 42, 43, 44, 45, 46, 47, 48, 49, 50)
                .verifyComplete();
    }

    @Test
    public void consumeIntRangeLimited() {
        Flux<Integer> integerFlux = FluxEventConsumer.fluxIntRangeConsumer(FluxEventProducer.produceIntRangeRateLimited(1, 25, 10));

        StepVerifier.create(integerFlux)
                .expectSubscription()
                // even when subscriber requests more events than rate limit, the producer rate limit still applies.
                .thenRequest(15)
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .expectNext(11, 12, 13, 14, 15)
                .thenRequest(10)
                .expectNext(16, 17, 18, 19, 20, 21, 22, 23, 24, 25)
                .verifyComplete();
    }

    @Test
    void fluxIntRangeConsumerWithCancel() {

        Flux<Integer> fluxWithCancel = FluxEventConsumer.fluxIntRangeConsumerWithCancel(FluxEventProducer.produceIntRange(1, 10));

        StepVerifier.create(fluxWithCancel)
                .expectNext(1, 2, 3, 4, 5)
                .thenCancel()
                .verify();
    }
}