package ar.edu.utn.frbb.tup.business;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;

public interface AsignaturaService {
    
    Asignatura getAsignaturaPorId(long idAsignatura) throws AsignaturaNotFoundException;

    void actualizarAsignatura(Asignatura a) throws AsignaturaNotFoundException;

    List<Asignatura> obtenerAsignaturas();
}
