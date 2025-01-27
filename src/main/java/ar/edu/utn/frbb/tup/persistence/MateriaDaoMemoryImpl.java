package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.utils.RandomCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();

    @Override
    public Materia save(final Materia materia, final int[] correlatividades) throws MateriaNotFoundException, DuplicatedException {
        duplicatedMateria(materia);
        materia.setMateriaId(RandomCreator.getInstance().generateRandomNumber(999));
        repositorioMateria.put(materia.getMateriaId(), materia);
        final List<Materia> listaCorrelatividades = new ArrayList<>();
        for (Integer i : correlatividades) {
            Materia materia2 = findMateriaById(i);
            listaCorrelatividades.add(materia2);
        }
        materia.setCorrelatividades(listaCorrelatividades);
        return materia;
    }

    @Override
    public Materia findMateriaById(final Integer id) throws MateriaNotFoundException {
        final Materia materia = repositorioMateria.get(id);
        if (materia == null) {
            throw new MateriaNotFoundException("No se encuentra ninguna materia con el ID: " + id);
        }
        return materia;
    }

    @Override
    public List<Materia> findMateriaByName(final String nombreMateria) throws MateriaNotFoundException {
        final List<Materia> listaFiltrada = new ArrayList<>();
        for (Materia materia : repositorioMateria.values()) {
            if (materia.getNombre().toLowerCase().startsWith(nombreMateria.toLowerCase())) {
                listaFiltrada.add(materia);
            }
        }
        if (listaFiltrada.isEmpty()) {
            throw new MateriaNotFoundException("No se encuentra ninguna materia que comience con el nombre '" + nombreMateria + "'.");
        }
        return listaFiltrada;
    }

    @Override
    public List<Materia> getAllMaterias() {
        final List<Materia> listaMaterias = new ArrayList<>();
        for (Materia materia : repositorioMateria.values()){
            listaMaterias.add(materia);
        }
        return listaMaterias;
    }

    @Override
    public void deleteMateriaById(final int materiaId) {
        repositorioMateria.remove(materiaId);
    }

    private boolean duplicatedMateria(final Materia materia) throws DuplicatedException {
        for (Materia materia1: repositorioMateria.values()){
            if (materia1.getNombre().equals(materia.getNombre())){
                throw new DuplicatedException("Existe una materia con el nombre " + materia.getNombre());
            }
        }
        return true;
    }
    
}
