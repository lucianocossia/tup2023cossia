package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorDao profesorDao;

    @Autowired
    private MateriaDao materiaDao;

    @Override
    public Profesor crearProfesor(final ProfesorDto profesorDto) throws DatoInvalidoException, DuplicatedException {
        checkStringField(profesorDto.getNombre(), "nombre");
        checkStringField(profesorDto.getApellido(), "apellido");
        checkStringField(profesorDto.getTitulo(), "título");

        final Profesor profesor = new Profesor();
        profesor.setTitulo(profesorDto.getTitulo());
        profesor.setApellido(profesorDto.getApellido());
        profesor.setNombre(profesorDto.getNombre());
        profesorDao.save(profesor);
        return profesor;
    }

    @Override
    public List<Profesor> obtenerProfesores() {
        return profesorDao.getAllProfesores();
    }

    @Override
    public List<Profesor> buscarProfesorApellido(final String apellido) throws ProfesorNotFoundException {
        return profesorDao.findProfesorBySurname(apellido);
    }

    @Override
    public Profesor buscarProfesorPorId(final Long id) throws ProfesorNotFoundException {
        return profesorDao.findProfesorById(id);
    }

    @Override
    public Profesor actualizarProfesorPorId(final Long idProfesor, final ProfesorDto profesorDto)
            throws ProfesorNotFoundException, DatoInvalidoException {
        final Profesor profesor = profesorDao.findProfesorById(idProfesor);
        profesor.setId(idProfesor);
        if (profesorDto.getNombre() != null &&
                !profesorDto.getNombre().isBlank() && !profesorDto.getNombre().matches(".*\\d+.*")) {
            profesor.setNombre(profesorDto.getNombre());
        }
        if (profesorDto.getApellido() != null &&
                !profesorDto.getApellido().isBlank() && !profesorDto.getApellido().matches(".*\\d+.*")) {
            profesor.setApellido(profesorDto.getApellido());
            ;
        }
        if (profesorDto.getTitulo() != null &&
                !profesorDto.getTitulo().isBlank() && !profesorDto.getTitulo().matches(".*\\d+.*")) {
            profesor.setTitulo(profesorDto.getTitulo());
        }
        profesorDao.update(idProfesor, profesor);
        return profesor;
    }

    @Override
    public void actualizarProfesor(final Profesor profesor) throws ProfesorNotFoundException {
        profesorDao.update(profesor.getId(), profesor);
    }

    @Override
    public void borrarProfesorPorId(Long id) throws ProfesorNotFoundException {
        for (Materia materia : profesorDao.getMateriasAsociadas(id)) {
            materiaDao.deleteMateriaById(materia.getMateriaId());
        }
        profesorDao.deleteProfesorById(id);
    }

    private boolean checkStringField(final String field, final String fieldName) throws DatoInvalidoException {
        if (field == null || field.isBlank()) {
            throw new DatoInvalidoException("El " + fieldName + " no puede ser nulo ni estar vacío.");
        }
        if (field.matches(".*\\d+.*")) {
            throw new DatoInvalidoException("El " + fieldName + " no puede contener números.");
        }
        return true;
    }

}
