package com.api.rest.reactiva.repository;

import com.api.rest.reactiva.documents.Contacto;
import com.api.rest.reactiva.repostory.ContactoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactoRepositoryTest {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private ReactiveMongoOperations reactiveMongoOperations;

    @BeforeAll
    public void insertarDatos(){
        Contacto contacto1 = new Contacto();
        contacto1.setNombre("prueba");
        contacto1.setApellido("prueba");
        contacto1.setTelefono(111111);
        contacto1.setEmail("prueba@prueba");

        Contacto contacto2 = new Contacto();
        contacto2.setNombre("prueba2");
        contacto2.setApellido("prueba2");
        contacto2.setTelefono(111111);
        contacto2.setEmail("prueba2@prueba");

        Contacto contacto3 = new Contacto();
        contacto3.setNombre("prueba2");
        contacto3.setApellido("prueba2");
        contacto3.setTelefono(111111);
        contacto3.setEmail("prueba2@prueba");

        StepVerifier.create(contactoRepository.save(contacto1).log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(contactoRepository.save(contacto2).log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(contactoRepository.save(contacto3).log())
                .expectSubscription()
                .expectNextMatches(contacto -> contacto.getId() != null)
                .verifyComplete();
    }

    @Test
    @Order(1)
    public void testListarContactos(){
        StepVerifier.create(contactoRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(2);
    }

    @Test
    @Order(2)
    public void testLFidEMail(){
        StepVerifier.create(contactoRepository.findFirstByEmail("prueba@prueba").log())
                .expectSubscription()
                .expectNextMatches(contacto -> contacto.getNombre().equals("prueba"))
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void testActualizarContacto(){
        Mono<Contacto> contactoMono = contactoRepository.findFirstByEmail("prueba@prueba")
                        .map(contacto -> {
                            contacto.setTelefono(123456);
                        return contacto;
                        }).flatMap(contacto -> {
                            return contactoRepository.save(contacto);
                });
        StepVerifier.create(contactoMono.log())
                .expectSubscription()
                .expectNextMatches(contacto -> !contacto.getTelefono().equals(111111))
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void testEliminarContacoById(){
        Mono<Void> contactoEliminado = contactoRepository.findFirstByEmail("prueba2@prueba")
                .flatMap(contacto -> {
                    return contactoRepository.deleteById(contacto.getId());
                }).log();
        StepVerifier.create(contactoEliminado)
                .expectSubscription()
                .verifyComplete();
    }
}
