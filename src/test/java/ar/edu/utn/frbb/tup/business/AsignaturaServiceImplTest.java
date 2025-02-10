package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.impl.AsignaturaServiceImpl;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsignaturaServiceImplTest {

    @InjectMocks
    private AsignaturaServiceImpl asignaturaService;

    @Mock
    private AsignaturaDao asignaturaDao;

    @Mock
    private MateriaService materiaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAsignaturaPorId_OK() throws AsignaturaNotFoundException {
        Asignatura asignatura = new Asignatura();
        when(asignaturaDao.getAsignaturaPorId(100L)).thenReturn(asignatura);

        Asignatura result = asignaturaService.getAsignaturaPorId(100L);

        assertNotNull(result);
        verify(asignaturaDao).getAsignaturaPorId(100L);
    }

    @Test
    void testActualizarAsignatura() throws AsignaturaNotFoundException {
        Asignatura asignatura = new Asignatura();
        doNothing().when(asignaturaDao).update(asignatura);

        asignaturaService.actualizarAsignatura(asignatura);

        verify(asignaturaDao).update(asignatura);
    }

    @Test
    void testObtenerAsignaturas() {
        List<Materia> listaMaterias = new ArrayList<>();

        Materia materia = new Materia("LaboratorioIII", 2, 1, null);
        listaMaterias.add(materia);

        when(materiaService.obtenerMaterias()).thenReturn(listaMaterias);

       
        doNothing().when(asignaturaDao).saveAsignaturas(listaMaterias);

        List<Asignatura> listaAsignaturas = new ArrayList<>();
        Asignatura asignatura = new Asignatura(materia, 1L);
        listaAsignaturas.add(asignatura);
        when(asignaturaDao.getListAsignaturas()).thenReturn(listaAsignaturas);

        List<Asignatura> result = asignaturaService.obtenerAsignaturas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(asignatura, result.get(0));

        verify(materiaService).obtenerMaterias();
        verify(asignaturaDao).saveAsignaturas(listaMaterias);
        verify(asignaturaDao).getListAsignaturas();
    }
}
