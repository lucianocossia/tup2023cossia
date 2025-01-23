package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import org.springframework.stereotype.Service;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao{

    @Override
    public Profesor get(long id) {
            return new Profesor("Luciano", "Salotto", "Lic. Ciencias Computación");
    }
}
