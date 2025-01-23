package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

import java.util.Optional;

public class Asignatura {

    private Materia materia;
    private EstadoAsignatura estado;
    private Integer nota;

    public Asignatura() {
    }
    public Asignatura(Materia materia) {
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    public Optional<Integer> getNota() {
        return Optional.ofNullable(nota);
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public EstadoAsignatura getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }

    public String getNombreAsignatura(){
        return this.materia.getNombre();
    }

    public Materia getMateria() {
        return materia;
    }

    public void cursarAsignatura(){
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(int nota) throws EstadoIncorrectoException {
        if (!this.estado.equals(EstadoAsignatura.CURSADA)) {
            throw new EstadoIncorrectoException("La materia debe estar cursada");
        }
        if (nota>=4) {
            this.estado = EstadoAsignatura.APROBADA;
            this.nota = nota;
        }
    }

}
