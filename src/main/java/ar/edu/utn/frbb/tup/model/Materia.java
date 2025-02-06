package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "materiaId")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Materia {

    private Long materiaId;
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private Profesor profesor;
    private List<Materia> correlatividades;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        if (profesor != null) {
            this.profesor = profesor;
            profesor.setMateriasDictadas(this);
        }
    }

    public void setCorrelatividades(List<Materia> correlatividades) {
        this.correlatividades = correlatividades;
    }

    public Materia() {
    }

    public Materia(String nombre, int anio, int cuatrimestre, Profesor profesor) {
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.nombre = nombre;
        this.profesor = profesor;

        correlatividades = new ArrayList<>();
    }

    public void agregarCorrelatividad(Materia m) {
        this.correlatividades.add(m);
    }

    public List<Materia> getCorrelatividades() {
        return this.correlatividades;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Materia materia = (Materia) o;
        return materiaId == materia.materiaId && anio == materia.anio && cuatrimestre == materia.cuatrimestre
                && Objects.equals(nombre, materia.nombre) && Objects.equals(profesor, materia.profesor)
                && Objects.equals(correlatividades, materia.correlatividades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materiaId, nombre, anio, cuatrimestre, profesor, correlatividades);
    }
}
