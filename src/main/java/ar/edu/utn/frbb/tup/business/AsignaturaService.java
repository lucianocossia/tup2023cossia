package ar.edu.utn.frbb.tup.business;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Asignatura;

public interface AsignaturaService {
    Asignatura getAsignatura(int materiaId, long dni);

    void actualizarAsignatura(Asignatura a);

    List<Asignatura> obtenerAsignaturas();
}
