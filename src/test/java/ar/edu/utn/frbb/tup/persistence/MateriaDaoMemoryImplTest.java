package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MateriaDaoMemoryImplTest {

    private MateriaDaoMemoryImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = new MateriaDaoMemoryImpl();

        Field repositoryField = MateriaDaoMemoryImpl.class
                .getDeclaredField("repositorioMateria");
        repositoryField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Long, Materia> map = (Map<Long, Materia>) repositoryField.get(null);
        map.clear();
    }

    @Test
    void testSave_SinCorrelativas_OK() throws MateriaNotFoundException, DuplicatedException {
        Materia materia = new Materia("LaboratorioIII", 2, 1, null);
        Long[] correlatividadesVacias = new Long[] {};

        Materia guardada = dao.save(materia, correlatividadesVacias);

        assertNotNull(guardada);
        assertTrue(guardada.getMateriaId() > 0, "Se esperaba un ID > 0 generado aleatoriamente");
        assertEquals("LaboratorioIII", guardada.getNombre());
        assertTrue(guardada.getCorrelatividades().isEmpty());
    }

    @Test
    void testSave_ConCorrelativas_OK() throws MateriaNotFoundException, DuplicatedException {

        Materia matBase = new Materia("LaboratorioII", 1, 2, null);
        Materia matBaseGuardada = dao.save(matBase, new Long[] {});
        Long idBase = matBaseGuardada.getMateriaId();

        Materia materia = new Materia("LaboratorioIII", 2, 1, null);
        Long[] correlatividades = new Long[] { idBase };

        Materia guardada = dao.save(materia, correlatividades);

        assertNotNull(guardada);
        assertEquals("LaboratorioIII", guardada.getNombre());
        assertEquals(1, guardada.getCorrelatividades().size());
        assertEquals("LaboratorioII", guardada.getCorrelatividades().get(0).getNombre());
    }

    @Test
    void testSave_MateriaDuplicada() throws DuplicatedException, MateriaNotFoundException {
        Materia mat1 = new Materia(":LaboratorioIII", 2, 1, null);
        dao.save(mat1, new Long[]{});

        Materia mat2 = new Materia(":LaboratorioIII", 2, 1, null);

        assertThrows(DuplicatedException.class, () -> {
            dao.save(mat2, new Long[]{});
        });
    }

    @Test
    void testFindMateriaById_OK() throws MateriaNotFoundException, DuplicatedException {
        Materia mat = new Materia("LaboratorioIII", 2, 1, null);
        Materia guardada = dao.save(mat, new Long[]{});

        Long idGenerado = guardada.getMateriaId();
        Materia encontrada = dao.findMateriaById(idGenerado);
        assertNotNull(encontrada);
        assertEquals("LaboratorioIII", encontrada.getNombre());
        assertEquals(idGenerado, encontrada.getMateriaId());
    }

    @Test
    void testFindMateriaById_NotFound() {
        assertThrows(MateriaNotFoundException.class, () -> {
            dao.findMateriaById(999L);
        });
    }

    @Test
    void testFindMateriaByName_OK() throws DuplicatedException, MateriaNotFoundException {
        Materia mat1 = new Materia("Programación I", 1, 1, null);
        Materia mat2 = new Materia("Programación II", 1, 2, null);
        dao.save(mat1, new Long[]{});
        dao.save(mat2, new Long[]{});

        List<Materia> resultado = dao.findMateriaByName("Progra");
        assertEquals(2, resultado.size());
    }

    @Test
    void testFindMateriaByName_NoMatches() throws DuplicatedException, MateriaNotFoundException {
        Materia mat = new Materia("LaboratorioIII", 2, 1, null);
        dao.save(mat, new Long[]{});

        assertThrows(MateriaNotFoundException.class, () -> {
            dao.findMateriaByName("Derecho Informático");
        });
    }

    @Test
    void testGetAllMaterias_OK() throws DuplicatedException, MateriaNotFoundException {
        dao.save(new Materia("ProgramacionIII", 2, 1, null), new Long[]{});
        dao.save(new Materia("LaboratorioIII", 2, 1, null), new Long[]{});

        List<Materia> lista = dao.getAllMaterias();
        assertEquals(2, lista.size());
    }

    @Test
    void testGetAllMaterias_Empty() {
        List<Materia> lista = dao.getAllMaterias();
        assertTrue(lista.isEmpty());
    }

    @Test
    void testDeleteMateriaById() throws DuplicatedException, MateriaNotFoundException {
        Materia mat = new Materia("LaboratorioIII", 2, 1, null);
        Materia guardada = dao.save(mat, new Long[]{});
        Long idGenerado = guardada.getMateriaId();

        dao.deleteMateriaById(idGenerado);

        assertThrows(MateriaNotFoundException.class, () -> {
            dao.findMateriaById(idGenerado);
        });
    }
}
