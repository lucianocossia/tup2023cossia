package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

public interface MateriaDao {
    Materia save(Materia materia);

    Materia findById(int idMateria) throws MateriaNotFoundException;
}
