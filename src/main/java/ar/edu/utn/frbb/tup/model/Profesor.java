package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Profesor {

    private Long id;
    private String nombre;
    private String apellido;
    private String titulo;
    private List<Materia> materiasDictadas;

    public Profesor() {
        materiasDictadas = new ArrayList<>();
    }

    public Profesor(String nombre, String apellido, String titulo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.titulo = titulo;
        this.materiasDictadas = new ArrayList<>();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Materia> getMateriasDictadas() {
        return materiasDictadas;
    }

    public void setMateriasDictadas(Materia materiaDictada) {
        this.materiasDictadas.add(materiaDictada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, titulo, materiasDictadas);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Profesor profesor = (Profesor) o;

        return id == profesor.id &&
                Objects.equals(nombre, profesor.nombre) &&
                Objects.equals(apellido, profesor.apellido) &&
                Objects.equals(titulo, profesor.titulo) &&
                Objects.equals(materiasDictadas, profesor.materiasDictadas);
    }

}
