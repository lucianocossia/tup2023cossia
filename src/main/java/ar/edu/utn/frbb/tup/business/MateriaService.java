package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import java.util.List;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws ProfesorNotFoundException, MateriaNotFoundException, DuplicatedException;
    Materia buscarMateriaPorId(Integer id) throws MateriaNotFoundException;
    List<Materia> buscarMateriaPorNombre(String nombre) throws MateriaNotFoundException;
    List<Materia> getAllMaterias();
}
