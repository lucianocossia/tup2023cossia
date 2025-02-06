package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDao materiaDao;

    @Autowired
    private ProfesorService profesorService;

    @Override
    public Materia crearMateria(final MateriaDto materiaDto)
            throws ProfesorNotFoundException, MateriaNotFoundException, DuplicatedException {
        Materia materia = new Materia();
        materia.setNombre(materiaDto.getNombre());
        materia.setAnio(materiaDto.getAnio());
        materia.setCuatrimestre(materiaDto.getCuatrimestre());
        Profesor p = profesorService.buscarProfesorPorId(materiaDto.getProfesorId());
        materia.setProfesor(p);
        materiaDao.save(materia, materiaDto.getCorrelatividades());
        profesorService.actualizarProfesor(p);
        return materia;
    }

    @Override
    public Materia buscarMateriaPorId(final Long id) throws MateriaNotFoundException {
        return materiaDao.findMateriaById(id);
    }

    @Override
    public List<Materia> buscarMateriaPorNombre(final String nombreMateria) throws MateriaNotFoundException {
        return materiaDao.findMateriaByName(nombreMateria);
    }

    @Override
    public List<Materia> obtenerMaterias() {
        return materiaDao.getAllMaterias();
    }
}
