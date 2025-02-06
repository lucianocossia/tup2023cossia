package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

public interface MateriaDao {

    Materia save(Materia materia, Long[] correlatividades) throws MateriaNotFoundException, DuplicatedException;

    Materia findMateriaById(final Long id) throws MateriaNotFoundException;

    List<Materia> findMateriaByName(String nombreMateria) throws MateriaNotFoundException;

    List<Materia> getAllMaterias();

    void deleteMateriaById(Long materiaId);
    
}
