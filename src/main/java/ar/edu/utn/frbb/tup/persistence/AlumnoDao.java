package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;

public interface AlumnoDao {

    Alumno saveAlumno(Alumno a);

    Alumno findAlumno(String apellidoAlumno) throws AlumnoNotFoundException;

    Alumno findAlumnoById(Long dni) throws AlumnoNotFoundException;

    Alumno findAlumnoByDNI(Long dni) throws AlumnoNotFoundException;

    void update(Long dni, Alumno a) throws AlumnoNotFoundException;

}
