package ar.edu.utn.frbb.tup.business;

import java.util.List;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.EstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;

public interface AlumnoService {

    Alumno crearAlumno(AlumnoDto alumno) throws DuplicatedException, DatoInvalidoException;

    Alumno buscarAlumno(String apellidoAlumno) throws AlumnoNotFoundException;

    Alumno buscarAlumnoPorId(long id) throws AlumnoNotFoundException;

    Alumno buscarAlumnoPorDni(long dni) throws AlumnoNotFoundException;

    List<Asignatura> obtenerAsignaturasAlumnoPorId(final Long id) throws AlumnoNotFoundException, AsignaturaNotFoundException;

    Asignatura obtenerAsignaturaAlumnoPorId(final Long id, final Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException;

    Alumno actualizarAlumnoPorId(final Long idAlumno, final AlumnoDto alumnoDto) throws AlumnoNotFoundException;

    Asignatura actualizarEstadoAsignaturaPorID(final Long idAlumno, final Long idAsignatura, final AsignaturaDto asignaturaDto) throws EstadoIncorrectoException, CorrelatividadException,
            AlumnoNotFoundException, AsignaturaNotFoundException, NotaException, EstadoAsignaturaException, DuplicatedException;

}
