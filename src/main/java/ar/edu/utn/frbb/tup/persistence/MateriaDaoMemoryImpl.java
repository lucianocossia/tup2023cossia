package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();
    @Override
    public Materia save(Materia materia) {
        Random random = new Random();
        materia.setMateriaId(random.nextInt());
        repositorioMateria.put(materia.getMateriaId(), materia);
        return materia;
    }

    @Override
    public Materia findById(int idMateria) throws MateriaNotFoundException {
        for (Materia m:
             repositorioMateria.values()) {
            if (idMateria == m.getMateriaId()) {
                return m;
            }
        }
        throw new MateriaNotFoundException("No se encontró la materia con id " + idMateria);
    }
}
