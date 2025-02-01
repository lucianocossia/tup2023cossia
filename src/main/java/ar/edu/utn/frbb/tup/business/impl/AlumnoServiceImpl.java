package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
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
    public void aprobarAsignatura(int materiaId, int nota, long dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AlumnoNotFoundException {
        Asignatura a = asignaturaService.getAsignatura(materiaId, dni);
        for (Materia m:
             a.getMateria().getCorrelatividades()) {
            Asignatura correlativa = asignaturaService.getAsignatura(m.getMateriaId(), dni);
            if (!EstadoAsignatura.APROBADA.equals(correlativa.getEstado())) {
                throw new CorrelatividadesNoAprobadasException("La materia " + m.getNombre() + " debe estar aprobada para aprobar " + a.getNombreAsignatura());
            }
        }
        a.aprobarAsignatura(nota);
        asignaturaService.actualizarAsignatura(a);
        Alumno alumno = alumnoDao.findAlumnoByDNI(dni);
        alumno.actualizarAsignatura(a);
        alumnoDao.saveAlumno(alumno);
    }

     @Override
    public Alumno crearAlumno(final AlumnoDto alumnoDto) throws DatoInvalidoException {
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
}
