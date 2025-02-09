package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AsignaturaDaoMemoryImplTest {

    private AsignaturaDaoMemoryImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = new AsignaturaDaoMemoryImpl();

        Field repositorioField = AsignaturaDaoMemoryImpl.class
                .getDeclaredField("repositorioAsignaturas");  
        repositorioField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Long, Asignatura> map = (Map<Long, Asignatura>) repositorioField.get(null);
        map.clear();
    }

    @Test
    void testSaveAsignatura() {
        Materia materia = new Materia("LaboratorioIII", 2, 1, null);

        Asignatura asig = dao.saveAsignatura(materia);

        assertNotNull(asig);
        assertNotNull(asig.getAsignaturaId());
        assertTrue(asig.getAsignaturaId() > 0);
        assertEquals("LaboratorioIII", asig.getNombreAsignatura());

        List<Asignatura> lista = dao.getListAsignaturas();
        assertEquals(1, lista.size());
        assertSame(asig, lista.get(0));
    }

    @Test
    void testSaveAsignaturasList() {
        Materia mat1 = new Materia("ProgramacionIII", 2, 1, null);
        Materia mat2 = new Materia("LaboratorioIII", 2, 1, null);
        List<Materia> listaMat = new ArrayList<>();
        listaMat.add(mat1);
        listaMat.add(mat2);

        dao.saveAsignaturas(listaMat);

        List<Asignatura> resultado = dao.getListAsignaturas();
        assertEquals(2, resultado.size());

        boolean contieneFisica = resultado.stream()
                .anyMatch(a -> "ProgramacionIII".equals(a.getNombreAsignatura()));
        boolean contieneQuimica = resultado.stream()
                .anyMatch(a -> "LaboratorioIII".equals(a.getNombreAsignatura()));

        assertTrue(contieneFisica);
        assertTrue(contieneQuimica);
    }

    @Test
    void testGetListAsignaturas_Vacio() {
        List<Asignatura> lista = dao.getListAsignaturas();
        assertTrue(lista.isEmpty());
    }

    @Test
    void testGetAsignaturaPorId_OK() throws AsignaturaNotFoundException {
        // Insertamos una Asignatura
        Materia mat = new Materia("LaboratorioIII", 2, 1, null);
        Asignatura asigGuardada = dao.saveAsignatura(mat);
        Long idGenerado = asigGuardada.getAsignaturaId();

        Asignatura asigRecuperada = dao.getAsignaturaPorId(idGenerado);

        assertNotNull(asigRecuperada);
        assertEquals("LaboratorioIII", asigRecuperada.getNombreAsignatura());
        assertEquals(idGenerado, asigRecuperada.getAsignaturaId());
    }

    @Test
    void testGetAsignaturaPorId_NotFound() {
        assertThrows(AsignaturaNotFoundException.class, () -> {
            dao.getAsignaturaPorId(9999L);
        });
    }

    @Test
    void testUpdate_OK() throws AsignaturaNotFoundException {
        Materia mat = new Materia("LaboratorioIII", 2, 1, null);
        Asignatura asigGuardada = dao.saveAsignatura(mat);

        asigGuardada.setNota(java.util.Optional.of(8));
        asigGuardada.setEstado(EstadoAsignatura.CURSADA);

        dao.update(asigGuardada);

        Asignatura asigRecuperada = dao.getAsignaturaPorId(asigGuardada.getAsignaturaId());
        assertEquals(EstadoAsignatura.CURSADA, asigRecuperada.getEstado());
        assertEquals(java.util.Optional.of(8), asigRecuperada.getNota());
    }

    @Test
    void testUpdate_NotFound() {
        Asignatura asigNoExistente = new Asignatura();
        asigNoExistente.setAsignaturaId(500L);
        assertThrows(AsignaturaNotFoundException.class, () -> {
            dao.update(asigNoExistente);
        });
    }
}
