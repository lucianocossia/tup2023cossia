package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.EstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Alumno {

    private long alumnoId;
    private String nombre;
    private String apellido;
    private long dni;

    private List<Asignatura> asignaturas;

    public Alumno() {
        this.asignaturas = new ArrayList<>();
    }

    public Alumno(String nombre, String apellido, long dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;

        asignaturas = new ArrayList<>();

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public List<Asignatura> getAsignaturas() {
        return this.asignaturas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public long getDni() {
        return dni;
    }

    public void agregarAsignatura(Asignatura a) {
        this.asignaturas.add(a);
    }

    public List<Asignatura> obtenerListaAsignaturas() {
        return this.asignaturas;
    }

    public void aprobarAsignatura(Asignatura asignatura, Optional<Integer> nota) throws EstadoIncorrectoException, NotaException, CorrelatividadException, AsignaturaNotFoundException {
        chequearAsignatura(asignatura);
        for (Materia correlativa :
                asignatura.getCorrelatividades()) {
            chequearCorrelatividad(correlativa, asignatura);
        }
        asignatura.aprobarAsignatura(nota);
    }

    public void cursarAsignatura(Asignatura asignatura)
            throws EstadoAsignaturaException, AsignaturaNotFoundException, CorrelatividadException {
        chequearAsignatura(asignatura);
        for (Materia correlativa : asignatura.getCorrelatividades()) {
            chequearCorrelatividad(correlativa, asignatura);
        }
        asignatura.cursarAsignatura();
    }

    private void chequearCorrelatividad(Materia correlativa, Asignatura asignatura)
        throws CorrelatividadException {
    boolean found = false;
    for (Asignatura a : asignaturas) {
        if (correlativa.getNombre().equals(a.getNombreAsignatura())) {
            found = true;
            if (!EstadoAsignatura.APROBADA.equals(a.getEstado())) {
                throw new CorrelatividadException(
                        "La asignatura " + correlativa.getNombre() + " debe estar aprobada.");
            }
        }
    }
    if (!found) {
        throw new CorrelatividadException(
                "La asignatura correlativa " + correlativa.getNombre() + " no se encuentra en la lista del alumno."
        );
    }
}


    private void chequearAsignatura(Asignatura asignatura) throws AsignaturaNotFoundException {
        if (!asignaturas.contains(asignatura)) {
            throw new AsignaturaNotFoundException(
                    "El alumno " + this.nombre + " " + this.apellido + " (ID: " + this.alumnoId + "), no tiene " +
                            "a la asignatura: " + asignatura.getNombreAsignatura());
        }
    }

    public void actualizarAsignatura(Asignatura asignatura) {
        for (Asignatura a : asignaturas) {
            if (a.getNombreAsignatura().equals(asignatura.getNombreAsignatura())) {
                a.setEstado(asignatura.getEstado());
                a.setNota(asignatura.getNota());
            }
        }
    }

    public long getId() {
        return alumnoId;
    }

    public void setId(long alumnoId) {
        this.alumnoId = alumnoId;
    }
}
