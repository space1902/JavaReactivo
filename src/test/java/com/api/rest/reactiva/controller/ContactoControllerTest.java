package com.api.rest.reactiva.controller;

import com.api.rest.reactiva.documents.Contacto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private Contacto contacto;

    @Test
    @Order(0)
    public void testGuardarContacto(){
        Flux<Contacto> contactoFlux = webTestClient.post()
                .uri("/api/v1/contactos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Contacto("1", "prueba2", "prueba", "prueba@prueba", 111111)))
                .exchange()
                .expectStatus().isAccepted()
                .returnResult(Contacto.class).getResponseBody()
                .log();

        contactoFlux.next().subscribe(contacto1 -> {
            this.contacto = contacto1;
        });

        Assertions.assertNotNull(contacto);
    }
}
