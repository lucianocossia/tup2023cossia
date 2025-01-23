package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Long, Alumno> repositorioAlumnos = new HashMap<>();

    @Override
    public Alumno saveAlumno(Alumno alumno) {
        Random random = new Random();
        alumno.setId(random.nextLong());
        return repositorioAlumnos.put(alumno.getDni(), alumno);
    }

    @Override
    public Alumno findAlumno(String apellidoAlumno) {
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getApellido().equals(apellidoAlumno)){
                return a;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existen alumnos con esos datos."
        );
    }

    @Override
    public Alumno loadAlumno(Long dni) {
        return null;
    }

}
