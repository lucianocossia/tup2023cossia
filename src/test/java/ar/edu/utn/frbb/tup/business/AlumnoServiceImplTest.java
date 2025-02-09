package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.business.impl.AlumnoServiceImpl;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AlumnoServiceImplTest {

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    @Mock
    private AlumnoDao alumnoDao;

    @Mock
    private AsignaturaService asignaturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearAlumno_OK() throws DuplicatedException, DatoInvalidoException {

        AlumnoDto dto = new AlumnoDto();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setDni(12345678L);

        List<Asignatura> asignaturasDummy = new ArrayList<>();
        asignaturasDummy.add(new Asignatura());
        when(asignaturaService.obtenerAsignaturas()).thenReturn(asignaturasDummy);

        when(alumnoDao.saveAlumno(any(Alumno.class)))
                .thenReturn(new Alumno("Juan", "Pérez", 12345678L));

        Alumno alumnoCreado = alumnoService.crearAlumno(dto);

        assertNotNull(alumnoCreado);
        assertEquals("Juan", alumnoCreado.getNombre());
        assertEquals("Pérez", alumnoCreado.getApellido());
        assertEquals(12345678L, alumnoCreado.getDni());

        assertEquals(1, alumnoCreado.getAsignaturas().size());

        verify(alumnoDao).saveAlumno(any(Alumno.class));
    }

    @Test
    void testCrearAlumno_NullDto() {

        AlumnoDto dto = null;

        assertThrows(IllegalArgumentException.class, () -> alumnoService.crearAlumno(dto));

        verifyNoInteractions(alumnoDao, asignaturaService);
    }

    @Test
    void testCrearAlumno_DatosInvalidos() throws DuplicatedException {

        AlumnoDto dto = new AlumnoDto();
        dto.setNombre("");
        dto.setApellido("Pérez");
        dto.setDni(12345678L);

        assertThrows(DatoInvalidoException.class, () -> alumnoService.crearAlumno(dto));

        verifyNoMoreInteractions(alumnoDao);
    }

    @Test
    void testBuscarAlumno_OK() throws AlumnoNotFoundException {

        Alumno alumnoMock = new Alumno("Juan", "Pérez", 12345678L);
        when(alumnoDao.findAlumno("Pérez")).thenReturn(alumnoMock);

        Alumno result = alumnoService.buscarAlumno("Pérez");

        assertNotNull(result);
        assertEquals("Pérez", result.getApellido());
        verify(alumnoDao).findAlumno("Pérez");
    }

    @Test
    void testBuscarAlumno_NoEncontrado() throws AlumnoNotFoundException {

        when(alumnoDao.findAlumno("Pérez")).thenThrow(new AlumnoNotFoundException("No existe"));

        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.buscarAlumno("Pérez"));
        verify(alumnoDao).findAlumno("Pérez");
    }

    @Test
    void testBuscarAlumnoPorId_OK() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("Maria", "Lopez", 99999999L);
        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumno);

        Alumno resultado = alumnoService.buscarAlumnoPorId(1L);
        assertEquals("Lopez", resultado.getApellido());
        verify(alumnoDao).findAlumnoById(1L);
    }

    @Test
    void testBuscarAlumnoPorId_NoEncontrado() throws AlumnoNotFoundException {
        when(alumnoDao.findAlumnoById(100L))
                .thenThrow(new AlumnoNotFoundException("Not found"));
        assertThrows(AlumnoNotFoundException.class,
                () -> alumnoService.buscarAlumnoPorId(100L));
        verify(alumnoDao).findAlumnoById(100L);
    }

    @Test
    void testObtenerAsignaturasAlumnoPorId_OK() throws AlumnoNotFoundException, AsignaturaNotFoundException {
        List<Asignatura> lista = Collections.singletonList(new Asignatura());
        when(alumnoDao.getAsignaturasAlumnoPorId(10L)).thenReturn(lista);

        List<Asignatura> result = alumnoService.obtenerAsignaturasAlumnoPorId(10L);
        assertEquals(1, result.size());
        verify(alumnoDao).getAsignaturasAlumnoPorId(10L);
    }

    @Test
    void testObtenerAsignaturasAlumnoPorId_NotFound()
            throws AlumnoNotFoundException, AsignaturaNotFoundException {
        when(alumnoDao.getAsignaturasAlumnoPorId(10L))
                .thenThrow(new AsignaturaNotFoundException("No asignaturas"));

        assertThrows(AsignaturaNotFoundException.class,
                () -> alumnoService.obtenerAsignaturasAlumnoPorId(10L));
        verify(alumnoDao).getAsignaturasAlumnoPorId(10L);
    }

    @Test
    void testObtenerAsignaturaAlumnoPorId_OK() throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Asignatura asig = new Asignatura();
        when(alumnoDao.getAsignaturaAlumnoPorId(10L, 20L)).thenReturn(asig);

        Asignatura result = alumnoService.obtenerAsignaturaAlumnoPorId(10L, 20L);
        assertNotNull(result);
        verify(alumnoDao).getAsignaturaAlumnoPorId(10L, 20L);
    }

    @Test
    void testObtenerAsignaturaAlumnoPorId_NotFound()
            throws AlumnoNotFoundException, AsignaturaNotFoundException {
        when(alumnoDao.getAsignaturaAlumnoPorId(10L, 20L))
                .thenThrow(new AsignaturaNotFoundException("No existe"));

        assertThrows(AsignaturaNotFoundException.class,
                () -> alumnoService.obtenerAsignaturaAlumnoPorId(10L, 20L));
        verify(alumnoDao).getAsignaturaAlumnoPorId(10L, 20L);
    }

    @Test
    void testActualizarAlumnoPorId_OK() throws AlumnoNotFoundException {

        Alumno alumno = new Alumno("Juan", "Pérez", 11111111L);
        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumno);

        AlumnoDto dto = new AlumnoDto();
        dto.setNombre("Juanito");
        dto.setApellido("Perezzz");
        dto.setDni(22222222L);

        doNothing().when(alumnoDao).update(1L, alumno);

        Alumno updated = alumnoService.actualizarAlumnoPorId(1L, dto);

        assertNotNull(updated);
        assertEquals("Juanito", updated.getNombre());
        assertEquals("Perezzz", updated.getApellido());
        assertEquals(22222222L, updated.getDni());
        verify(alumnoDao).findAlumnoById(1L);
        verify(alumnoDao).update(eq(1L), any(Alumno.class));
    }

    @Test
    void testActualizarAlumnoPorId_NoEncontrado() throws AlumnoNotFoundException {
        when(alumnoDao.findAlumnoById(99L))
                .thenThrow(new AlumnoNotFoundException("Not found"));

        AlumnoDto dto = new AlumnoDto();
        assertThrows(AlumnoNotFoundException.class,
                () -> alumnoService.actualizarAlumnoPorId(99L, dto));
        verify(alumnoDao).findAlumnoById(99L);
    }

    @Test
    void testActualizarEstadoAsignaturaPorID_AprobadaOk() throws Exception {
        Alumno alumnoMock = spy(new Alumno("Carlos", "Lopez", 33333333L));
        Asignatura asignaturaMock = mock(Asignatura.class);

        when(asignaturaMock.getNombreAsignatura()).thenReturn("LaboratorioIII");

        alumnoMock.agregarAsignatura(asignaturaMock);

        when(alumnoDao.findAlumnoById(10L)).thenReturn(alumnoMock);
        when(asignaturaService.getAsignaturaPorId(20L)).thenReturn(asignaturaMock);

        AsignaturaDto dto = new AsignaturaDto();
        dto.setEstado(EstadoAsignatura.APROBADA);
        dto.setNota(Optional.of(8));

        doNothing().when(asignaturaService).actualizarAsignatura(any(Asignatura.class));
        doNothing().when(alumnoDao).update(anyLong(), any(Alumno.class));

        Asignatura result = alumnoService.actualizarEstadoAsignaturaPorID(10L, 20L, dto);

        verify(alumnoMock).aprobarAsignatura(asignaturaMock, Optional.of(8));
        verify(asignaturaService).actualizarAsignatura(asignaturaMock);
        verify(alumnoMock).actualizarAsignatura(asignaturaMock);
        verify(alumnoDao).update(eq(alumnoMock.getId()), eq(alumnoMock));

        assertNotNull(result);
        assertSame(asignaturaMock, result);
    }

    @Test
    void testActualizarEstadoAsignaturaPorID_AprobadaSinNota_LanzaExcepcion()
            throws AlumnoNotFoundException, AsignaturaNotFoundException {

        Alumno alumno = new Alumno("Carlos", "Lopez", 33333333L);
        when(alumnoDao.findAlumnoById(10L)).thenReturn(alumno);

        Asignatura asig = new Asignatura();
        when(asignaturaService.getAsignaturaPorId(20L)).thenReturn(asig);

        AsignaturaDto dto = new AsignaturaDto();
        dto.setEstado(EstadoAsignatura.APROBADA);
        dto.setNota(null);

        assertThrows(IllegalArgumentException.class,
                () -> alumnoService.actualizarEstadoAsignaturaPorID(10L, 20L, dto));
        verify(alumnoDao, never()).update(anyLong(), any(Alumno.class));
        verify(asignaturaService, never()).actualizarAsignatura(any());
    }

    @Test
    void testActualizarEstadoAsignaturaPorID_CursadaOk() throws Exception {
        Alumno alumnoSpy = spy(new Alumno("Carlos", "Lopez", 33333333L));
        Asignatura asigMock = mock(Asignatura.class);

        when(asigMock.getNombreAsignatura()).thenReturn("LaboratorioIII");

        alumnoSpy.agregarAsignatura(asigMock);

        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumnoSpy);
        when(asignaturaService.getAsignaturaPorId(2L)).thenReturn(asigMock);

        AsignaturaDto dto = new AsignaturaDto();
        dto.setEstado(EstadoAsignatura.CURSADA);
        dto.setNota(null);

        // se evita que pueda causar efectos secundarios
        doNothing().when(alumnoDao).update(anyLong(), any(Alumno.class));

        Asignatura result = alumnoService.actualizarEstadoAsignaturaPorID(1L, 2L, dto);

        verify(alumnoSpy).cursarAsignatura(asigMock);
        verify(asignaturaService).actualizarAsignatura(asigMock);
        verify(alumnoSpy).actualizarAsignatura(asigMock);
        verify(alumnoDao).update(eq(alumnoSpy.getId()), eq(alumnoSpy));

        assertNotNull(result);
        assertSame(asigMock, result);
    }

    @Test
    void testActualizarEstadoAsignaturaPorID_EstadoInvalido()
            throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = new Alumno("Beto", "Gonzalez", 4444L);
        when(alumnoDao.findAlumnoById(10L)).thenReturn(alumno);

        Asignatura asig = new Asignatura();
        when(asignaturaService.getAsignaturaPorId(20L)).thenReturn(asig);

        AsignaturaDto dto = new AsignaturaDto();

        dto.setEstado(EstadoAsignatura.NO_CURSADA);
        dto.setNota(Optional.empty());

        assertThrows(EstadoAsignaturaException.class,
                () -> alumnoService.actualizarEstadoAsignaturaPorID(10L, 20L, dto));

        verify(alumnoDao, never()).update(anyLong(), any(Alumno.class));
    }
}