package com.api.rest.reactiva.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "contacto")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Contacto {

    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private Integer telefono;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contacto contacto = (Contacto) o;
        return Objects.equals(id, contacto.id) && Objects.equals(nombre, contacto.nombre) && Objects.equals(apellido, contacto.apellido) && Objects.equals(email, contacto.email) && Objects.equals(telefono, contacto.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, email, telefono);
    }
}
