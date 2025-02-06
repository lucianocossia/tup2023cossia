package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;

public interface AsignaturaDao {

    void saveAsignaturas(List<Materia> listaMaterias);

    Asignatura saveAsignatura(Materia m);

    Asignatura getAsignaturaPorId(long id) throws AsignaturaNotFoundException;

    List<Asignatura> getListAsignaturas();

    void update(Asignatura a) throws AsignaturaNotFoundException;
}
