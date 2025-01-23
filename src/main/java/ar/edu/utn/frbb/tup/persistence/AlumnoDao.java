package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;

public interface AlumnoDao {

    Alumno saveAlumno(Alumno a);

    Alumno findAlumno(String apellidoAlumno);

    Alumno loadAlumno(Long dni);
}
