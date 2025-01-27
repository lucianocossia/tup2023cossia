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
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.utils.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlumnoServiceImpl implements AlumnoService {

    private static final AlumnoDao alumnoDao = new AlumnoDaoMemoryImpl();
    private static final AsignaturaService asignaturaService = new AsignaturaServiceImpl();

    @Override
    public void aprobarAsignatura(int materiaId, int nota, long dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException {
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
        Alumno alumno = alumnoDao.loadAlumno(dni);
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
    public Alumno buscarAlumno(String apellido) {
        return alumnoDao.findAlumno(apellido);
    }
}
