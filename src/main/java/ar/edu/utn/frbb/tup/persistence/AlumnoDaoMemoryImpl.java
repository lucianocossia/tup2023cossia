package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;

import org.springframework.stereotype.Service;

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
    public Alumno findAlumno(String apellidoAlumno) throws AlumnoNotFoundException {
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getApellido().equals(apellidoAlumno)){
                return a;
            }
        }
        throw new AlumnoNotFoundException("No existen alumnos con esos datos.");
    }

    @Override
    public Alumno findAlumnoById(Long id) throws AlumnoNotFoundException {
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getId() == id){
                return a;
            }
        }
        throw new AlumnoNotFoundException("No existen alumnos con ese ID.");
    }

    @Override
    public Alumno findAlumnoByDNI(Long dni) throws AlumnoNotFoundException {
        if (repositorioAlumnos.containsKey(dni)){
            return repositorioAlumnos.get(dni);
        }
        throw new AlumnoNotFoundException("No existen alumnos con ese DNI.");
    }

}
