package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.utils.RandomCreator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao {

    private static final Map<Long, Profesor> repositorioProfesores = new HashMap<>();

    @Override
    public Profesor save(final Profesor profesor) throws DuplicatedException {
        if (duplicatedProfesor(profesor)) {
            throw new DuplicatedException("Ya existe un profesor con los datos ingresados [" +
                    profesor.getNombre() + " " + profesor.getApellido() + "].");
        }
        profesor.setId(RandomCreator.getInstance().generateRandomNumber(999));
        repositorioProfesores.put(profesor.getId(), profesor);
        return profesor;
    }

    @Override
    public List<Profesor> getAllProfesores() {
        return repositorioProfesores.values().stream().toList();
    }

    @Override
    public Profesor findProfesorById(final Long id) throws ProfesorNotFoundException {
        final Profesor profesor = repositorioProfesores.get(id);
        if (profesor == null) {
            throw new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: " + id + ".");
        }
        return profesor;
    }

    @Override
    public List<Profesor> findProfesorBySurname(final String apellidoProfesor) throws ProfesorNotFoundException {
        List<Profesor> listaFiltrada = repositorioProfesores.values().stream()
                .filter(profesor -> profesor.getApellido().toLowerCase().startsWith(apellidoProfesor.toLowerCase()))
                .toList();

        if (listaFiltrada.isEmpty()) {
            throw new ProfesorNotFoundException(
                    "No se encuentra ningún profesor que comience con el apellido '" + apellidoProfesor + "'.");
        }
        return listaFiltrada;
    }

    @Override
    public List<Materia> getMateriasAsociadas(final Long id) throws ProfesorNotFoundException {
        final Profesor profesor = repositorioProfesores.get(id);
        if (profesor == null){
            throw new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: " + id + ".");
        }
        List<Materia> listaMaterias = profesor.getMateriasDictadas();
        Collections.sort(listaMaterias, (materia1, materia2) -> materia1.getNombre().compareTo(materia2.getNombre()));
        return listaMaterias;
    }

    private boolean duplicatedProfesor(final Profesor profesor) {
        for (Profesor profesor1 : repositorioProfesores.values()) {
            if (profesor.getNombre().equals(profesor1.getNombre()) &&
                    profesor.getApellido().equals(profesor1.getApellido())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(final Long id, final Profesor profesor) throws ProfesorNotFoundException {
        repositorioProfesores.put(id, profesor);
    }

    @Override
    public void deleteProfesorById(final Long id) throws ProfesorNotFoundException {
        Profesor profesor = findProfesorById(id);
        if (profesor == null) {
            throw new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: " + id + ".");
        }
        repositorioProfesores.remove(id);
    }

}
