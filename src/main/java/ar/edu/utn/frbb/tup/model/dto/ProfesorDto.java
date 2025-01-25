package ar.edu.utn.frbb.tup.model.dto;

import org.springframework.stereotype.Component;

@Component
public class ProfesorDto {
    private String nombre;
    private String apellido;
    private String titulo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}