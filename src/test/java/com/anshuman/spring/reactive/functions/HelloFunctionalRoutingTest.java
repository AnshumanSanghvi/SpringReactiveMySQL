package com.anshuman.spring.reactive.functions;

import com.anshuman.spring.reactive.SpringReactiveMySqlApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringReactiveMySqlApplication.class)
@Disabled // to run the tests, remove the command line runner bean from main App.
class HelloFunctionalRoutingTest {

    @Autowired
    private HelloFunctionalRouting helloRouting;

    @Test
    void routeHelloPlain() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(helloRouting.routeHelloPlain())
                .build();

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/hello").queryParam("name", "Anshuman").build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Hello, Anshuman");

    }
}