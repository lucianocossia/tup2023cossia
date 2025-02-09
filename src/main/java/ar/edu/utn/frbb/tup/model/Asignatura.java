package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "asignaturaId")
public class Asignatura {

    private Long asignaturaId;
    private Materia materia;
    private EstadoAsignatura estado;
    private Optional<Integer> nota;

    public Asignatura() {
    }

    public Asignatura(Materia materia, Long asignaturaId) {
        this.asignaturaId = asignaturaId;
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
        this.nota = Optional.empty();
    }

    public Optional<Integer> getNota() {
        return nota;
    }

    public void setNota(Optional<Integer> nota) {
        this.nota = nota;
    }

    public EstadoAsignatura getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public void setAsignaturaId(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public String getNombreAsignatura() {
        return this.materia.getNombre();
    }

    public Materia getMateria() {
        return materia;
    }

    public List<Materia> getCorrelatividades() {
        return this.materia.getCorrelatividades();
    }

    public void cursarAsignatura() {
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(Optional<Integer> nota)
            throws EstadoIncorrectoException, NotaException {
        if (!this.estado.equals(EstadoAsignatura.CURSADA)) {
            throw new EstadoIncorrectoException("La materia debe estar cursada");
        }

        if (nota.isPresent()) {
            int n = nota.get();
            if (n < 0 || n > 10) {
                throw new NotaException("Nota fuera de rango");
            }
            if (n >= 4) {
                this.estado = EstadoAsignatura.APROBADA;
                this.nota = Optional.of(n);
            } else {
                this.nota = Optional.of(n);
            }
        }
    }
}