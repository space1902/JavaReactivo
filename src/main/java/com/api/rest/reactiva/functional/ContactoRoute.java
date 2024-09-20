package com.api.rest.reactiva.functional;

import com.mongodb.internal.connection.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ContactoRoute {

    @Bean
    public RouterFunction<ServerResponse> routerContacto(ContactoHandler contactoHandler){
        return RouterFunctions
                .route(GET("/functional/contactos"),contactoHandler::listarContactos)
                .andRoute(GET("/functional/contactos/{id}"),contactoHandler::obtenerContactoById)
                .andRoute(GET("/functional/contactos/email/{email}"),contactoHandler::obtenerContactoByEmail)
                .andRoute(POST("/functional/contactos"),contactoHandler::insertarContacto)
                .andRoute(PUT("/functional/contactos/{id}"),contactoHandler::editarContacto)
                .andRoute(DELETE("/functional/contactos/{id}"),contactoHandler::eliminarContactoById);

    }
}
