package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

public interface ProfesorDao {

    Profesor save(Profesor profesor) throws DuplicatedException;

    List<Profesor> getAllProfesores();

    Profesor findProfesorById(Long id) throws ProfesorNotFoundException;

    List<Profesor> findProfesorBySurname(String apellidoProfesor) throws ProfesorNotFoundException;

    List<Materia> getMateriasAsociadas(Long id) throws ProfesorNotFoundException;

    void update(Long idProfesor, Profesor profesor) throws ProfesorNotFoundException;

    void deleteProfesorById(Long id) throws ProfesorNotFoundException;
}
