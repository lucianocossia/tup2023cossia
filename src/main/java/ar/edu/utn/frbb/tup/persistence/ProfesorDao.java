package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

public interface ProfesorDao {

    Profesor save(Profesor profesor) throws DuplicatedException;
    Profesor findProfesorById(long id) throws ProfesorNotFoundException;
    List<Profesor> findProfesorBySurname(String apellidoProfesor) throws ProfesorNotFoundException;
}