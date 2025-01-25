    package ar.edu.utn.frbb.tup.business.impl;

    import ar.edu.utn.frbb.tup.business.ProfesorService;
    import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
    import ar.edu.utn.frbb.tup.model.Profesor;
    import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
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

        @Override
        public Profesor crearProfesor(final ProfesorDto profesorDto) throws DatoInvalidoException, DuplicatedException {
            validarCampo(profesorDto.getNombre(), "nombre");
            validarCampo(profesorDto.getApellido(), "apellido");
            checkTitulo(profesorDto);

            final Profesor profesor = new Profesor();
            profesor.setTitulo(profesorDto.getTitulo());
            profesor.setApellido(profesorDto.getApellido());
            profesor.setNombre(profesorDto.getNombre());
            profesorDao.save(profesor);
            return profesor;
        }

        @Override
        public List<Profesor> buscarProfesorApellido(final String apellido) throws ProfesorNotFoundException {
            return profesorDao.findProfesorBySurname(apellido);
        }

        @Override
        public Profesor buscarProfesorPorId(final Long id) throws ProfesorNotFoundException {
            return profesorDao.findProfesorById(id);
        }

        private boolean validarCampo(final String campo, final String nombreCampo) throws DatoInvalidoException {
            if (campo == null || campo.isBlank()) {
                throw new DatoInvalidoException("El " + nombreCampo + " no puede ser nulo ni estar vacío.");
            }
            if (campo.matches(".*\\d+.*")) {
                throw new DatoInvalidoException("El " + nombreCampo + " no puede contener números.");
            }
            return true;
        }

        private boolean checkTitulo(final ProfesorDto profesorDto) throws DatoInvalidoException {
            if (profesorDto.getTitulo() == null || profesorDto.getTitulo().isBlank()){
                throw new DatoInvalidoException("El título no puede ser nulo ni estar vacío.");
            }
            if (profesorDto.getTitulo().matches(".*\\d+.*")){
                throw new DatoInvalidoException("El título no puede contener números decimales, recuerde anotarlo con" +
                        " números romanos. Ejemplo: 4 --> IV");
            }
            return true;
        }
    }
