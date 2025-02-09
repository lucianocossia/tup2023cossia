package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;

public interface AlumnoDao {

    Alumno saveAlumno(Alumno a) throws DuplicatedException;

    Alumno findAlumno(String apellidoAlumno) throws AlumnoNotFoundException;

    Alumno findAlumnoById(Long id) throws AlumnoNotFoundException;

    Alumno findAlumnoByDNI(Long dni) throws AlumnoNotFoundException;

    List<Asignatura> getAsignaturasAlumnoPorId(Long id) throws AlumnoNotFoundException, AsignaturaNotFoundException;
    
    Asignatura getAsignaturaAlumnoPorId(Long id, Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException;

    void update(Long id, Alumno a) throws AlumnoNotFoundException;

}
