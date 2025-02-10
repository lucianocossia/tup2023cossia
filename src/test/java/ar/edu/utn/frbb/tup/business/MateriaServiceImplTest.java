package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.impl.MateriaServiceImpl;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MateriaServiceImplTest {

    @InjectMocks
    private MateriaServiceImpl materiaService;

    @Mock
    private MateriaDao materiaDao;

    @Mock
    private ProfesorService profesorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearMateria_OK() throws ProfesorNotFoundException, MateriaNotFoundException, DuplicatedException {

        MateriaDto dto = new MateriaDto();
        dto.setNombre("LaboratorioIII");
        dto.setAnio(2);
        dto.setCuatrimestre(1);
        dto.setProfesorId(100L);

        dto.setCorrelatividades(new Long[]{});

        Profesor profesor = new Profesor("Luciano", "Salotto", "Lic.");
        when(profesorService.buscarProfesorPorId(100L)).thenReturn(profesor);

        // Stub: materiaDao.save simula el guardado (podemos retornar el mismo objeto => echo)
        doAnswer(invocation -> {
            Materia m = invocation.getArgument(0);
            return m;
        }).when(materiaDao).save(any(Materia.class), eq(dto.getCorrelatividades()));

        // Stub: profesorService.actualizarProfesor no hace nada (método void)
        doNothing().when(profesorService).actualizarProfesor(profesor);

        Materia resultado = materiaService.crearMateria(dto);

        assertNotNull(resultado, "La materia creada no debe ser null");
        assertEquals("LaboratorioIII", resultado.getNombre());
        assertEquals(2, resultado.getAnio());
        assertEquals(1, resultado.getCuatrimestre());
        assertEquals(profesor, resultado.getProfesor());

        verify(profesorService).buscarProfesorPorId(100L);
        verify(materiaDao).save(any(Materia.class), eq(dto.getCorrelatividades()));
        verify(profesorService).actualizarProfesor(profesor);
    }

    @Test
    void testBuscarMateriaPorId_OK() throws MateriaNotFoundException {
        Materia materia = new Materia("LaboratorioIII", 2, 1, null);
        when(materiaDao.findMateriaById(1L)).thenReturn(materia);

        Materia resultado = materiaService.buscarMateriaPorId(1L);
        assertNotNull(resultado);
        assertEquals("LaboratorioIII", resultado.getNombre());
        verify(materiaDao).findMateriaById(1L);
    }

    @Test
    void testBuscarMateriaPorId_NotFound() throws MateriaNotFoundException {
        when(materiaDao.findMateriaById(999L))
                .thenThrow(new MateriaNotFoundException("No se encontró la materia"));

        assertThrows(MateriaNotFoundException.class, () -> materiaService.buscarMateriaPorId(999L));
        verify(materiaDao).findMateriaById(999L);
    }

    @Test
    void testBuscarMateriaPorNombre_OK() throws MateriaNotFoundException {
        List<Materia> lista = new ArrayList<>();
        lista.add(new Materia("Física", 2023, 1, null));
        when(materiaDao.findMateriaByName("Física")).thenReturn(lista);

        List<Materia> resultado = materiaService.buscarMateriaPorNombre("Física");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(materiaDao).findMateriaByName("Física");
    }


    @Test
    void testBuscarMateriaPorNombre_NotFound() throws MateriaNotFoundException {
        when(materiaDao.findMateriaByName("Base de Datos")).thenThrow(new MateriaNotFoundException("No se encontró"));

        assertThrows(MateriaNotFoundException.class, () -> materiaService.buscarMateriaPorNombre("Base de Datos"));
        verify(materiaDao).findMateriaByName("Base de Datos");
    }

    @Test
    void testObtenerMaterias() {
        List<Materia> lista = new ArrayList<>();
        lista.add(new Materia("ProgramacionII", 1, 2, null));
        lista.add(new Materia("Base de Datos", 2, 2, null));
        when(materiaDao.getAllMaterias()).thenReturn(lista);

        List<Materia> resultado = materiaService.obtenerMaterias();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(materiaDao).getAllMaterias();
    }
}
