package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;

import java.util.List;

public class AsignaturaServiceImpl implements AsignaturaService {
    @Override
    public Asignatura getAsignatura(int materiaId, long dni) {
        return null;
    }

    @Override
    public void actualizarAsignatura(Asignatura a) {

    }
}
