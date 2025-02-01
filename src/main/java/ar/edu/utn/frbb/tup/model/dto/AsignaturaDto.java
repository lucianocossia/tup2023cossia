package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import org.springframework.stereotype.Component;

@Component
public class AsignaturaDto {
    private int nota;

    private EstadoAsignatura estado;

    public void setNota(int nota) {
        this.nota = nota;
    }

    public void setestado(EstadoAsignatura estado) {
        this.estado = estado;
    }

    public int getNota() {
        return nota;
    }

    public EstadoAsignatura getestado() {
        return this.estado;
    }
}
