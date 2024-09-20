package com.api.rest.reactiva.controller;

import com.api.rest.reactiva.documents.Contacto;
import com.api.rest.reactiva.repostory.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class ContactoController {

    @Autowired
    private ContactoRepository contactoRepository;

    @GetMapping("/contactos")
    public Flux<Contacto> listarContactos(){
        return contactoRepository.findAll();
    }

    @GetMapping("/contacto/{id}")
    public Mono<ResponseEntity<Contacto>> obtenerContacto(@PathVariable String id){
        return contactoRepository.findById(id)
                .map(contacto -> new ResponseEntity<>(contacto, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/contactoemail/{email}")
    public Mono<ResponseEntity<Contacto>> obtenerContactoPorEmail(@PathVariable String email){
        return contactoRepository.findFirstByEmail(email)
                .map(contacto -> new ResponseEntity<>(contacto, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/contactos")
    public Mono<ResponseEntity<Contacto>> guardarContacto(@RequestBody Contacto contacto){
        return contactoRepository.insert(contacto)
                .map(contacto1 -> new ResponseEntity<>(contacto1, HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
    }

    @PutMapping("/contactos/{id}")
    public Mono<ResponseEntity<Contacto>> editarContacto(@PathVariable String id ,@RequestBody Contacto contacto){

        return contactoRepository.findById(id)
                .flatMap(contacto1 -> {
                    return contactoRepository.save(contacto)
                            .map(contacto2 -> new ResponseEntity<>(contacto1, HttpStatus.ACCEPTED));
                }).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/contactos/{id}")
    public Mono<Void> eliminarContacto(@PathVariable String id){

        return contactoRepository.deleteById(id);
    }

}
