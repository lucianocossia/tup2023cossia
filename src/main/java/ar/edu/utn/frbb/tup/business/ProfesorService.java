package ar.edu.utn.frbb.tup.business;

import java.util.List;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorWithoutMateriasException;

public interface ProfesorService {

    Profesor crearProfesor(ProfesorDto profesorDto) throws DatoInvalidoException, DuplicatedException;

    List<Profesor> obtenerProfesores();

    List<Profesor> buscarProfesorApellido(String apellido) throws ProfesorNotFoundException;

    Profesor buscarProfesorPorId(Long id) throws ProfesorNotFoundException;

    List<MateriaDto> obtenerMateriasPorProfesorDto(Long idProfesor)
        throws ProfesorNotFoundException, ProfesorWithoutMateriasException;

    Profesor actualizarProfesorPorId(Long idProfesor, ProfesorDto profesorDto)
            throws ProfesorNotFoundException, DatoInvalidoException;

    void actualizarProfesor(final Profesor profesor) throws ProfesorNotFoundException;

    void borrarProfesorPorId(Long id) throws ProfesorNotFoundException, ProfesorWithoutMateriasException;
    
}
