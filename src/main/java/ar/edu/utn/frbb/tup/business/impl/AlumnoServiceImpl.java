package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.EstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;
import ar.edu.utn.frbb.tup.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoDao alumnoDao;

    @Autowired
    private AsignaturaService asignaturaService;

    @Override
    public Alumno crearAlumno(final AlumnoDto alumnoDto) throws DuplicatedException, DatoInvalidoException {
        if (alumnoDto == null) {
            throw new IllegalArgumentException("El objeto alumnoDto es nulo.");
        }
        ValidationUtils.checkStringField(alumnoDto.getNombre(), "nombre");
        ValidationUtils.checkStringField(alumnoDto.getApellido(), "apellido");
        ValidationUtils.comprobarDni(alumnoDto.getDni());
        final Alumno a = new Alumno();
        a.setNombre(alumnoDto.getNombre());
        a.setApellido(alumnoDto.getApellido());
        a.setDni(alumnoDto.getDni());
        final List<Asignatura> asignaturas = asignaturaService.obtenerAsignaturas();
        a.setAsignaturas(asignaturas);
        alumnoDao.saveAlumno(a);
        return a;
    }

    @Override
    public Alumno buscarAlumno(String apellido) throws AlumnoNotFoundException {
        return alumnoDao.findAlumno(apellido);
    }

    @Override
    public Alumno buscarAlumnoPorId(long id) throws AlumnoNotFoundException {
        return alumnoDao.findAlumnoById(id);
    }

    @Override
    public Alumno buscarAlumnoPorDni(long dni) throws AlumnoNotFoundException {
        return alumnoDao.findAlumnoByDNI(dni);
    }

    @Override
    public List<Asignatura> obtenerAsignaturasAlumnoPorId(final Long id) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoDao.getAsignaturasAlumnoPorId(id);
    }

    @Override
    public Asignatura obtenerAsignaturaAlumnoPorId(final Long id, final Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoDao.getAsignaturaAlumnoPorId(id, idAsignatura);
    }

    @Override
    public Alumno actualizarAlumnoPorId(final Long idAlumno, final AlumnoDto alumnoDto) throws AlumnoNotFoundException {
        final Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        alumno.setId(idAlumno);
        if (alumnoDto.getNombre() != null &&
                !alumnoDto.getNombre().isBlank() && !alumnoDto.getNombre().matches(".*\\d+.*")) {
            alumno.setNombre(alumnoDto.getNombre());
        }
        if (alumnoDto.getApellido() != null &&
                !alumnoDto.getApellido().isBlank() && !alumnoDto.getApellido().matches(".*\\d+.*")) {
            alumno.setApellido(alumnoDto.getApellido());
            ;
        }
        String dniToString = Long.toString(alumnoDto.getDni());
        int digitQuantity = dniToString.length();
        if (digitQuantity == 7 || digitQuantity == 8) {
            alumno.setDni(alumnoDto.getDni());
        }
        alumnoDao.update(idAlumno, alumno);
        return alumno;
    }

    @Override
    public Asignatura actualizarEstadoAsignaturaPorID(final Long idAlumno, final Long idAsignatura,
            final AsignaturaDto asignaturaDto) throws EstadoIncorrectoException, CorrelatividadException,
            AlumnoNotFoundException, AsignaturaNotFoundException, NotaException, EstadoAsignaturaException, DuplicatedException {
                
        final Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        final Asignatura asignatura = asignaturaService.getAsignaturaPorId(idAsignatura);
        if (asignaturaDto.getEstado().equals(EstadoAsignatura.APROBADA)) {
            if (asignaturaDto.getNota() == null) {
                throw new IllegalArgumentException("La nota es obligatoria cuando el estado es 'APROBADA'.");
            }
            alumno.aprobarAsignatura(asignatura, asignaturaDto.getNota());
        } else if (asignaturaDto.getEstado().equals(EstadoAsignatura.CURSADA)) {
            alumno.cursarAsignatura(asignatura);
        } else {
            throw new EstadoAsignaturaException("El estado de la materia solo puede ser 'Cursada' o 'Aprobada'.");
        }
        
        asignaturaService.actualizarAsignatura(asignatura);
        alumno.actualizarAsignatura(asignatura);
        alumnoDao.update(alumno.getDni(), alumno);
        return asignatura;
    }

}
