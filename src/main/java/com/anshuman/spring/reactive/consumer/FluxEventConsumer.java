package com.anshuman.spring.reactive.consumer;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxEventConsumer {

    public static Flux<Integer> fluxIntRangeConsumer(Flux<Integer> integerFlux) {

        Subscriber<Integer> subscriber = new GenericSubscriber<>();
        integerFlux.subscribe(subscriber);
        return integerFlux;
    }

    public static Flux<Integer> fluxIntRangeConsumerWithCancel(Flux<Integer> integerFlux)
    {
        integerFlux.subscribe(new BaseSubscriber<>() {
            @Override
            protected void hookOnNext(Integer value) {
                super.hookOnNext(value);
                log.debug("flux event data: {}", value);
                if (value > 5)
                    cancel();
            }
        });

        return integerFlux;
    }

}
