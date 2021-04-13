package com.anshuman.spring.reactive.consumer;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class GenericSubscriber<T> implements Subscriber<T> {

    private final int loops;
    private final int requestItemSize;

    public GenericSubscriber(int loops, int requestItemSize) {
        this.loops = loops;
        this.requestItemSize = requestItemSize;
    }

    public GenericSubscriber() {
        this.loops = 5;
        this.requestItemSize = 10;
    }

    @Override
    public void onSubscribe(Subscription s) {
        for (int i = 0; i < loops; i++) {
            log.debug("Requesting the next {} elements", requestItemSize);
            s.request(requestItemSize);
        }
    }

    @Override
    public void onNext(T item) {
        log.debug("flux event data: {}", item);
    }

    @Override
    public void onError(Throwable t) {
        log.error("Exception encountered while consuming flux events, with errorMessage={}", t.getMessage(), t);
    }

    @Override
    public void onComplete() {
        log.info("Completed consuming flux events");
    }

}
