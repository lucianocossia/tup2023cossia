package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;

public interface AlumnoService {
    void aprobarAsignatura(int materiaId, int nota, long dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AlumnoNotFoundException;

    Alumno crearAlumno(AlumnoDto alumno) throws DatoInvalidoException;

    Alumno buscarAlumno(String apellidoAlumno) throws AlumnoNotFoundException;

    Alumno buscarAlumnoPorId(long id) throws AlumnoNotFoundException;

    Alumno buscarAlumnoPorDni(long dni) throws AlumnoNotFoundException;
}
