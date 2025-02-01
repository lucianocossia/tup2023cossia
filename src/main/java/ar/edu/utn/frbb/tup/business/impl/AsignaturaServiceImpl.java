package ar.edu.utn.frbb.tup.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;

public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    private AsignaturaDao asignaturaDao;

    @Autowired
    private MateriaService materiaService;
 

    @Override
    public Asignatura getAsignatura(int materiaId, long dni) {
        return null;
    }

    @Override
    public void actualizarAsignatura(Asignatura a) {

    }

     @Override
    public List<Asignatura> obtenerAsignaturas() {
        final List<Materia> listaMaterias = materiaService.obtenerMaterias();
        asignaturaDao.saveAsignaturas(listaMaterias);
        return asignaturaDao.getListAsignaturas();
    }

}
