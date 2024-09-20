package com.api.rest.reactiva.functional;

import com.api.rest.reactiva.documents.Contacto;
import com.api.rest.reactiva.repostory.ContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.*;

@Component
@RequiredArgsConstructor
public class ContactoHandler {

    @Autowired
    private ContactoRepository contactoRepository;

    private Mono<ServerResponse> response404 = ServerResponse.notFound().build();
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    //Listar contactos

    public Mono<ServerResponse> listarContactos(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(contactoRepository.findAll(), Contacto.class);
    }

    //Listar un contacto

    public Mono<ServerResponse> obtenerContactoById(ServerRequest request){
        String id = request.pathVariable("id");
        return contactoRepository.findById(id)
                .flatMap(contacto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(contacto)))
                .switchIfEmpty(response404);
    }

    //Listar un contacto por email

    public Mono<ServerResponse> obtenerContactoByEmail(ServerRequest request){
        String email = request.pathVariable("email");
        return contactoRepository.findFirstByEmail(email)
                .flatMap(contacto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(contacto)))
                .switchIfEmpty(response404);
    }

    //Insertar un contacto

    public Mono<ServerResponse> insertarContacto(ServerRequest request){
        Mono<Contacto> contactoGuardado = request.bodyToMono(Contacto.class);

        return contactoGuardado.flatMap(
                contacto -> contactoRepository.save(contacto)
                .flatMap(contactoG -> ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(contactoG)))
                .switchIfEmpty(response406));
    }

    //editar un contacto

    public Mono<ServerResponse> editarContacto(ServerRequest request){
        Mono<Contacto> contactoGuardado = request.bodyToMono(Contacto.class);
        String id = request.pathVariable("id");

        Mono<Contacto> contactoAct = contactoGuardado.flatMap(
                contacto -> contactoRepository.findById(id)
                        .flatMap(oldContacto -> {
                            oldContacto.setTelefono(contacto.getTelefono());
                            oldContacto.setNombre(contacto.getNombre());
                            oldContacto.setApellido(contacto.getApellido());
                            oldContacto.setEmail(contacto.getEmail());
                            return contactoRepository.save(oldContacto);
                        })
        );

        return contactoAct.flatMap(
                contacto -> ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(contacto)))
                        .switchIfEmpty(response406);
    }

    //Eliminar contacto


    public Mono<ServerResponse> eliminarContactoById(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<Void> contactoEliminado = contactoRepository.deleteById(id);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(contactoEliminado, Void.class);
    }

}
