package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorWithoutMateriasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProfesorDaoMemoryImplTest {

    private ProfesorDaoMemoryImpl dao;

    @BeforeEach
    void setUp() throws Exception {

        dao = new ProfesorDaoMemoryImpl();

        Field repositoryField = ProfesorDaoMemoryImpl.class
                .getDeclaredField("repositorioProfesores");
        repositoryField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Long, Profesor> map = (Map<Long, Profesor>) repositoryField.get(null);
        map.clear();
    }

    @Test
    void testSave_ProfesorOk() throws DuplicatedException {
        Profesor profesor = new Profesor("Juan", "Pérez", "Licenciado");

        Profesor guardado = dao.save(profesor);

        assertNotNull(guardado);
        assertTrue(guardado.getId() > 0, "Se esperaba un ID > 0 generado aleatoriamente");
        assertEquals("Juan", guardado.getNombre());
        assertEquals("Pérez", guardado.getApellido());
    }

    @Test
    void testSave_ProfesorDuplicado() throws DuplicatedException {
        Profesor profesor1 = new Profesor("Carlos", "Lopez", "Ing.");
        dao.save(profesor1);

        Profesor profesor2 = new Profesor("Carlos", "Lopez", "Lic.");
        assertThrows(DuplicatedException.class, () -> {
            dao.save(profesor2);
        });
    }

    @Test
    void testGetAllProfesores_Empty() {
        List<Profesor> lista = dao.getAllProfesores();
        assertTrue(lista.isEmpty());
    }

    @Test
    void testGetAllProfesores_OK() throws DuplicatedException {
        dao.save(new Profesor("Mario", "Gomez", "Ing."));
        dao.save(new Profesor("Ana", "Ramirez", "Dra."));

        List<Profesor> lista = dao.getAllProfesores();
        assertEquals(2, lista.size());
    }

    @Test
    void testFindProfesorById_OK() throws DuplicatedException, ProfesorNotFoundException {
        Profesor profesor = new Profesor("Julia", "Torres", "Lic.");
        Profesor guardado = dao.save(profesor);

        Long idGenerado = guardado.getId();
        Profesor encontrado = dao.findProfesorById(idGenerado);

        assertNotNull(encontrado);
        assertEquals("Torres", encontrado.getApellido());
        assertEquals(idGenerado, encontrado.getId());
    }

    @Test
    void testFindProfesorById_NotFound() {
        assertThrows(ProfesorNotFoundException.class, () -> {
            dao.findProfesorById(999L);
        });
    }

    @Test
    void testFindProfesorBySurname_OK() throws DuplicatedException, ProfesorNotFoundException {
        dao.save(new Profesor("Laura", "Gonzalez", "Lic."));
        dao.save(new Profesor("David", "Goncalves", "Ing."));

        List<Profesor> resultado = dao.findProfesorBySurname("Gon");
        assertEquals(2, resultado.size());
    }

    @Test
    void testFindProfesorBySurname_NoMatches() throws DuplicatedException {
        dao.save(new Profesor("Pablo", "Martinez", "Lic."));

        assertThrows(ProfesorNotFoundException.class, () -> {
            dao.findProfesorBySurname("Gon");
        });
    }

    @Test
    void testGetMateriasAsociadas_OK()
            throws DuplicatedException, ProfesorNotFoundException, ProfesorWithoutMateriasException {
        Profesor profesor = new Profesor("Elena", "Rodriguez", "Lic.");
        Materia mat1 = new Materia("ProgramacionI", 1, 1, null);
        Materia mat2 = new Materia("LaboratorioIV", 2, 2, null);

        profesor.setMateriasDictadas(mat1);
        profesor.setMateriasDictadas(mat2);

        Profesor guardado = dao.save(profesor);

        List<Materia> materias = dao.getMateriasAsociadas(guardado.getId());
        assertEquals(2, materias.size());

        assertEquals("LaboratorioIV", materias.get(0).getNombre());
        assertEquals("ProgramacionI", materias.get(1).getNombre());
    }

    @Test
    void testGetMateriasAsociadas_ProfesorSinMaterias()
            throws DuplicatedException, ProfesorNotFoundException {
        Profesor profesor = new Profesor("Marcos", "Diaz", "Ing.");
        Profesor guardado = dao.save(profesor);

        assertThrows(ProfesorWithoutMateriasException.class, () -> {
            dao.getMateriasAsociadas(guardado.getId());
        });
    }

    @Test
    void testGetMateriasAsociadas_ProfesorNotFound() {
        assertThrows(ProfesorNotFoundException.class, () -> {
            dao.getMateriasAsociadas(999L);
        });
    }

    @Test
    void testUpdate_OK() throws DuplicatedException, ProfesorNotFoundException {
        Profesor profesor = new Profesor("Mario", "Perez", "Ing.");
        Profesor guardado = dao.save(profesor);
        Long idGenerado = guardado.getId();

        // Modificamos
        guardado.setNombre("Mario Modificado");
        guardado.setTitulo("Lic.");

        dao.update(idGenerado, guardado);

        Profesor actualizado = dao.findProfesorById(idGenerado);
        assertEquals("Mario Modificado", actualizado.getNombre());
        assertEquals("Perez", actualizado.getApellido());
        assertEquals("Lic.", actualizado.getTitulo());
    }

    @Test
    void testDeleteProfesorById_OK() throws DuplicatedException, ProfesorNotFoundException {
        Profesor profesor = new Profesor("Sara", "Escobar", "Lic.");
        Profesor guardado = dao.save(profesor);

        dao.deleteProfesorById(guardado.getId());

        assertThrows(ProfesorNotFoundException.class, () -> {
            dao.findProfesorById(guardado.getId());
        });
    }

    @Test
    void testDeleteProfesorById_NotFound() {
        assertThrows(ProfesorNotFoundException.class, () -> {
            dao.deleteProfesorById(999L);
        });
    }
}
