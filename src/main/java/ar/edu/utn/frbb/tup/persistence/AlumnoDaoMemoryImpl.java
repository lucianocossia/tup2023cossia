package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.utils.RandomCreator;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Long, Alumno> repositorioAlumnos = new HashMap<>();

    @Override
    public Alumno saveAlumno(Alumno alumno) throws DuplicatedException {
        if (duplicatedAlumno(alumno)) {
            throw new DuplicatedException("Ya existe un alumno con los datos ingresados [" +
                    alumno.getNombre() + " " + alumno.getApellido() + "].");
        }
        alumno.setId(RandomCreator.getInstance().generateRandomNumber(999));
        repositorioAlumnos.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public Alumno findAlumno(String apellidoAlumno) throws AlumnoNotFoundException {
        for (Alumno a : repositorioAlumnos.values()) {
            if (a.getApellido().equals(apellidoAlumno)) {
                return a;
            }
        }
        throw new AlumnoNotFoundException("No existen alumnos con esos datos.");
    }

    @Override
    public Alumno findAlumnoById(Long id) throws AlumnoNotFoundException {
        for (Alumno a : repositorioAlumnos.values()) {
            if (a.getId() == id) {
                return a;
            }
        }
        throw new AlumnoNotFoundException("No existen alumnos con ese ID.");
    }

    @Override
    public Alumno findAlumnoByDNI(Long dni) throws AlumnoNotFoundException {
        for (Alumno a : repositorioAlumnos.values()) {
            if (a.getDni() == dni) {
                return a;
            }
        }
        throw new AlumnoNotFoundException("No existen alumnos con ese DNI.");
    }

    @Override
    public List<Asignatura> getAsignaturasAlumnoPorId(final Long id)
            throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = findAlumnoById(id);
        if (alumno == null) {
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        List<Asignatura> asignaturas = alumno.obtenerListaAsignaturas();
        if (asignaturas.isEmpty()) {
            throw new AsignaturaNotFoundException("La lista de asignaturas del alumno " + alumno.getNombre() +
                    alumno.getApellido() + " está vacía.");
        }
        return asignaturas;
    }

    @Override
    public Asignatura getAsignaturaAlumnoPorId(final Long id, final Long idAsignatura)
            throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = findAlumnoById(id);
        if (alumno == null) {
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        for (Asignatura asignatura : alumno.obtenerListaAsignaturas()) {
            if (asignatura.getAsignaturaId().equals(idAsignatura)) {
                return asignatura;
            }
        }
        throw new AsignaturaNotFoundException(
                "El alumno " + alumno.getNombre() + " " + alumno.getApellido() + " (ID: " + id + "), no tiene " +
                        "ninguna asignatura con el ID: " + idAsignatura + ".");
    }

    @Override
    public void update(final Long id, final Alumno alumno) throws AlumnoNotFoundException {
        boolean found = false;
        for (Alumno a : repositorioAlumnos.values()) {
            if (a.getId() == id) {
                repositorioAlumnos.put(alumno.getId(), alumno);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AlumnoNotFoundException("No se encuentra el alumno con Id: " + id);
        }
    }
    

    private boolean duplicatedAlumno(final Alumno alumno) {
        for (Alumno alumno1 : repositorioAlumnos.values()) {
            if (alumno.getNombre().equals(alumno1.getNombre()) &&
                    alumno.getApellido().equals(alumno1.getApellido())) {
                return true;
            }
        }
        return false;
    }

}
