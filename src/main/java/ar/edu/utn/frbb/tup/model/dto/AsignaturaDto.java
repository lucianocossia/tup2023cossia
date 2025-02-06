package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.EstadoAsignatura;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class AsignaturaDto {
    private Optional<Integer> nota;

    private EstadoAsignatura estado;

    public void setNota(Optional<Integer> nota) {
        this.nota = nota;
    }

    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }

    public Optional<Integer> getNota() {
        return nota;
    }

    public EstadoAsignatura getEstado() {
        return this.estado;
    }
}
