package ar.edu.utn.frbb.tup.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.utils.RandomCreator;

@Service
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    
    private static final Map<Long, Asignatura> repositorioAsignaturas = new HashMap<>();


    @Override
    public void saveAsignaturas(final List<Materia> listaMaterias){
        for (Materia materia : listaMaterias){
            saveAsignatura(materia);
        }
    }

    @Override
    public Asignatura saveAsignatura(final Materia materia) {
        final Asignatura asignatura = new Asignatura(materia, RandomCreator.getInstance().generateRandomNumber(999));
        repositorioAsignaturas.put(asignatura.getAsignaturaId(), asignatura);
        return asignatura;
    }

    @Override
    public List<Asignatura> getListAsignaturas(){
        final List<Asignatura> listaAsignaturas = new ArrayList<>();
        for (Asignatura asignatura : repositorioAsignaturas.values()){
            listaAsignaturas.add(asignatura);
        }
        return listaAsignaturas;
    }

    @Override
    public Asignatura getAsignaturaPorId(final long id) throws AsignaturaNotFoundException {
        final Asignatura asignatura = repositorioAsignaturas.get(id);
        if (asignatura == null){
            throw new AsignaturaNotFoundException("No se encuentra ninguna asignatura con el ID: " + id);
        }
        return asignatura;
    }
    
}
