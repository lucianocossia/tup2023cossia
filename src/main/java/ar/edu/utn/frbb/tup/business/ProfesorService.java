package ar.edu.utn.frbb.tup.business;

import java.util.List;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

public interface ProfesorService {

    Profesor crearProfesor(ProfesorDto profesorDto) throws DatoInvalidoException, DuplicatedException;

    List<Profesor> buscarProfesorApellido(String apellido) throws ProfesorNotFoundException;
    Profesor buscarProfesorPorId(Long id) throws ProfesorNotFoundException;

    Profesor actualizarProfesorPorId(Long idProfesor, ProfesorDto profesorDto) throws ProfesorNotFoundException, DatoInvalidoException;

}
