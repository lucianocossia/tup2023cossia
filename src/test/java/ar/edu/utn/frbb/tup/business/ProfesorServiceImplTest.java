package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.business.impl.ProfesorServiceImpl;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorWithoutMateriasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProfesorServiceImplTest {

    @InjectMocks
    private ProfesorServiceImpl profesorService;

    @Mock
    private ProfesorDao profesorDao;

    @Mock
    private MateriaDao materiaDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProfesor_OK() throws DatoInvalidoException, DuplicatedException {

        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Ana");
        dto.setApellido("Martinez");
        dto.setTitulo("Lic.");

        doAnswer(invocation -> {
            Profesor p = invocation.getArgument(0);
            p.setId(100L);
            return p;
        }).when(profesorDao).save(any(Profesor.class));

        Profesor resultado = profesorService.crearProfesor(dto);

        assertNotNull(resultado, "El profesor creado no debe ser null");
        assertEquals("Ana", resultado.getNombre());
        assertEquals("Martinez", resultado.getApellido());
        assertEquals("Lic.", resultado.getTitulo());
        verify(profesorDao).save(any(Profesor.class));
    }

    @Test
    void testObtenerProfesores() {
        List<Profesor> lista = new ArrayList<>();
        lista.add(new Profesor("Juan", "Perez", "Lic."));
        lista.add(new Profesor("Maria", "Lopez", "Dr."));
        when(profesorDao.getAllProfesores()).thenReturn(lista);

        List<Profesor> resultado = profesorService.obtenerProfesores();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(profesorDao).getAllProfesores();
    }

    @Test
    void testBuscarProfesorApellido_OK() throws ProfesorNotFoundException {
        List<Profesor> lista = new ArrayList<>();
        lista.add(new Profesor("Juan", "Perez", "Lic."));
        when(profesorDao.findProfesorBySurname("Perez")).thenReturn(lista);

        List<Profesor> resultado = profesorService.buscarProfesorApellido("Perez");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(profesorDao).findProfesorBySurname("Perez");
    }

    @Test
    void testBuscarProfesorPorId_OK() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Ana", "Lopez", "Dr.");
        profesor.setId(101L);
        when(profesorDao.findProfesorById(101L)).thenReturn(profesor);

        Profesor resultado = profesorService.buscarProfesorPorId(101L);
        assertNotNull(resultado);
        assertEquals("Lopez", resultado.getApellido());
        verify(profesorDao).findProfesorById(101L);
    }

    @Test
    void testObtenerMateriasPorProfesor_OK() throws ProfesorNotFoundException, ProfesorWithoutMateriasException {
        List<Materia> listaMaterias = new ArrayList<>();
        listaMaterias.add(new Materia("LaboratorioIII", 2, 1, null));
        when(profesorDao.getMateriasAsociadas(101L)).thenReturn(listaMaterias);

        List<Materia> resultado = profesorService.obtenerMateriasPorProfesor(101L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(profesorDao).getMateriasAsociadas(101L);
    }

    @Test
    void testActualizarProfesorPorId_OK() throws ProfesorNotFoundException, DatoInvalidoException {
        Profesor profesor = new Profesor("Carlos", "Martinez", "Lic.");
        profesor.setId(101L);
        when(profesorDao.findProfesorById(101L)).thenReturn(profesor);

        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Carlos Modificado");
        dto.setApellido("Martinez");
        dto.setTitulo("Ing.");

        doNothing().when(profesorDao).update(eq(101L), any(Profesor.class));

        Profesor resultado = profesorService.actualizarProfesorPorId(101L, dto);

        assertNotNull(resultado);
        assertEquals("Carlos Modificado", resultado.getNombre());
        assertEquals("Martinez", resultado.getApellido());
        assertEquals("Ing.", resultado.getTitulo());
        verify(profesorDao).findProfesorById(101L);
        verify(profesorDao).update(eq(101L), any(Profesor.class));
    }

    @Test
    void testActualizarProfesor_OK() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Luis", "Gonzalez", "Msc.");
        profesor.setId(101L);

        doNothing().when(profesorDao).update(101L, profesor);

        profesorService.actualizarProfesor(profesor);

        verify(profesorDao).update(101L, profesor);
    }

    @Test
void testBorrarProfesorPorId_OK() throws ProfesorNotFoundException, ProfesorWithoutMateriasException {

    List<Materia> listaMaterias = new ArrayList<>();
    Materia mat1 = new Materia("ProgramacionI", 1, 1, null);
    mat1.setMateriaId(1L);
    Materia mat2 = new Materia("ProgramacionII", 1, 2, null);
    mat2.setMateriaId(2L);
    listaMaterias.add(mat1);
    listaMaterias.add(mat2);

    when(profesorDao.getMateriasAsociadas(101L)).thenReturn(listaMaterias);
    
    doNothing().when(materiaDao).deleteMateriaById(anyLong());
    doNothing().when(profesorDao).deleteProfesorById(101L);

    profesorService.borrarProfesorPorId(101L);

    verify(materiaDao).deleteMateriaById(1L);
    verify(materiaDao).deleteMateriaById(2L);

    verify(profesorDao).deleteProfesorById(101L);
}

    @Test
    void testCrearProfesor_DatoInvalido_NombreVacio() {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("");
        dto.setApellido("Ramirez");
        dto.setTitulo("Dr.");

        assertThrows(DatoInvalidoException.class, () -> profesorService.crearProfesor(dto));
        verifyNoInteractions(profesorDao);
    }

    @Test
    void testCrearProfesor_DatoInvalido_ApellidoConNumeros() {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Luis");
        dto.setApellido("Gonzalez123");
        dto.setTitulo("Lic.");

        assertThrows(DatoInvalidoException.class, () -> profesorService.crearProfesor(dto));
        verifyNoInteractions(profesorDao);
    }

    @Test
    void testCrearProfesor_DatoInvalido_TituloNulo() {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Ana");
        dto.setApellido("Sanchez");
        dto.setTitulo(null);

        assertThrows(DatoInvalidoException.class, () -> profesorService.crearProfesor(dto));
        verifyNoInteractions(profesorDao);
    }

    @Test
    void testActualizarProfesorPorId_ConDatosInvalidos() throws ProfesorNotFoundException, DatoInvalidoException {

        Profesor profesor = new Profesor("Roberto", "Garcia", "Lic.");
        profesor.setId(101L);
        when(profesorDao.findProfesorById(101L)).thenReturn(profesor);
        
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("");
        dto.setApellido("Lopez123");
        dto.setTitulo("Dr. Ing.");

        doNothing().when(profesorDao).update(eq(101L), any(Profesor.class));
        
        Profesor actualizado = profesorService.actualizarProfesorPorId(101L, dto);

        assertEquals("Roberto", actualizado.getNombre());
        assertEquals("Garcia", actualizado.getApellido());
        assertEquals("Dr. Ing.", actualizado.getTitulo());
        verify(profesorDao).findProfesorById(101L);
        verify(profesorDao).update(eq(101L), any(Profesor.class));
    }
}