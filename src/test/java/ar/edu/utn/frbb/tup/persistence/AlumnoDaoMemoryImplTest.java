package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AlumnoDaoMemoryImplTest {

    private AlumnoDaoMemoryImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = new AlumnoDaoMemoryImpl();

        Field repositorioField = AlumnoDaoMemoryImpl.class
                .getDeclaredField("repositorioAlumnos");
        repositorioField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Long, Alumno> map = (Map<Long, Alumno>) repositorioField.get(null);
        map.clear();
    }

    @Test
    void testSaveAlumno_OK() throws DuplicatedException {
        Alumno alumno = new Alumno("Juan", "Pérez", 12345678L);

        Alumno guardado = dao.saveAlumno(alumno);

        assertNotNull(guardado);
        assertTrue(guardado.getId() > 0, "Se esperaba un ID > 0 generado aleatoriamente");
        assertEquals("Juan", guardado.getNombre());
        assertEquals("Pérez", guardado.getApellido());
        assertEquals(12345678L, guardado.getDni());
    }

    @Test
    void testSaveAlumno_Duplicated() throws DuplicatedException {
        Alumno alumno1 = new Alumno("Carlos", "Lopez", 99999999L);
        dao.saveAlumno(alumno1);

        Alumno alumno2 = new Alumno("Carlos", "Lopez", 12312312L);

        assertThrows(DuplicatedException.class, () -> {
            dao.saveAlumno(alumno2);
        });
    }

    @Test
    void testFindAlumno_OK() throws DuplicatedException, AlumnoNotFoundException {
        Alumno a1 = new Alumno("Pepe", "Lopez", 11111111L);
        Alumno a2 = new Alumno("Ana", "Gomez", 22222222L);
        dao.saveAlumno(a1);
        dao.saveAlumno(a2);

        Alumno result = dao.findAlumno("Lopez");
        assertNotNull(result);
        assertEquals("Lopez", result.getApellido());
        assertEquals("Pepe", result.getNombre());
    }

    @Test
    void testFindAlumno_NoEncontrado() throws DuplicatedException {
        Alumno a1 = new Alumno("Pepe", "Lopez", 11111111L);
        dao.saveAlumno(a1);

        assertThrows(AlumnoNotFoundException.class, () -> {
            dao.findAlumno("Gonzalez");
        });
    }

    @Test
    void testFindAlumnoById_OK() throws DuplicatedException, AlumnoNotFoundException {
        Alumno alumno = new Alumno("Lucia", "Diaz", 33333333L);
        dao.saveAlumno(alumno);

        Long idGenerado = alumno.getId();

        Alumno encontrado = dao.findAlumnoById(idGenerado);
        assertNotNull(encontrado);
        assertEquals("Diaz", encontrado.getApellido());
        assertEquals(idGenerado, encontrado.getId());
    }

    @Test
    void testFindAlumnoById_NotFound() {
        assertThrows(AlumnoNotFoundException.class, () -> {
            dao.findAlumnoById(999L);
        });
    }

    @Test
    void testFindAlumnoByDNI_OK() throws DuplicatedException, AlumnoNotFoundException {
        Alumno alumno = new Alumno("Sofia", "Martinez", 44444444L);
        dao.saveAlumno(alumno);

        Alumno encontrado = dao.findAlumnoByDNI(44444444L);
        assertEquals("Sofia", encontrado.getNombre());
        assertEquals("Martinez", encontrado.getApellido());
    }

    @Test
    void testFindAlumnoByDNI_NotFound() {
        assertThrows(AlumnoNotFoundException.class, () -> {
            dao.findAlumnoByDNI(98765432L);
        });
    }

    @Test
    void testGetAsignaturasAlumnoPorId_OK()
            throws DuplicatedException, AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = new Alumno("Miguel", "Torres", 10101010L);
        Asignatura asig1 = new Asignatura();
        asig1.setAsignaturaId(1L);
        Asignatura asig2 = new Asignatura();
        asig2.setAsignaturaId(2L);

        alumno.agregarAsignatura(asig1);
        alumno.agregarAsignatura(asig2);

        dao.saveAlumno(alumno);
        Long idGenerado = alumno.getId();

        List<Asignatura> asignaturas = dao.getAsignaturasAlumnoPorId(idGenerado);
        assertEquals(2, asignaturas.size());
    }

    @Test
    void testGetAsignaturasAlumnoPorId_Empty() throws DuplicatedException {
        Alumno alumno = new Alumno("Lorenzo", "Castillo", 11112222L);
        dao.saveAlumno(alumno);

        assertThrows(AsignaturaNotFoundException.class, () -> {
            dao.getAsignaturasAlumnoPorId(alumno.getId());
        });
    }

    @Test
    void testGetAsignaturasAlumnoPorId_AlumnoNoEncontrado() {
        assertThrows(AlumnoNotFoundException.class, () -> {
            dao.getAsignaturasAlumnoPorId(999L);
        });
    }

    @Test
    void testGetAsignaturaAlumnoPorId_OK()
            throws DuplicatedException, AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = new Alumno("Carlos", "Lopez", 54545454L);
        Asignatura asig = new Asignatura();
        asig.setAsignaturaId(10L);
        alumno.agregarAsignatura(asig);

        dao.saveAlumno(alumno);

        Asignatura asignaturaEncontrada = dao.getAsignaturaAlumnoPorId(alumno.getId(), 10L);
        assertNotNull(asignaturaEncontrada);
        assertEquals(10L, asignaturaEncontrada.getAsignaturaId());
    }

    @Test
    void testGetAsignaturaAlumnoPorId_AsigNoEncontrada() throws DuplicatedException {
        Alumno alumno = new Alumno("Pedro", "Mendez", 21212121L);
        Asignatura asig = new Asignatura();
        asig.setAsignaturaId(10L);
        alumno.agregarAsignatura(asig);

        dao.saveAlumno(alumno);

        assertThrows(AsignaturaNotFoundException.class, () -> {
            dao.getAsignaturaAlumnoPorId(alumno.getId(), 99L);
        });
    }

    @Test
    void testGetAsignaturaAlumnoPorId_AlumnoNoEncontrado() {
        assertThrows(AlumnoNotFoundException.class, () -> {
            dao.getAsignaturaAlumnoPorId(888L, 10L);
        });
    }

    @Test
    void testUpdateById_OK() throws DuplicatedException, AlumnoNotFoundException {
        Alumno alumno = new Alumno("Raúl", "Fuentes", 55555555L);
        Alumno alumnoGuardado = dao.saveAlumno(alumno);
    
        Long idGenerado = alumnoGuardado.getId();
        assertNotNull(idGenerado, "Se espera un ID asignado tras saveAlumno");
    
        alumnoGuardado.setNombre("Raúl Modificado");
        alumnoGuardado.setDni(99999999L);
    
        dao.update(idGenerado, alumnoGuardado);
    
        Alumno alumnoActualizado = dao.findAlumnoById(idGenerado);
        assertEquals("Raúl Modificado", alumnoActualizado.getNombre());
        assertEquals(99999999L, alumnoActualizado.getDni());
        assertEquals(idGenerado, alumnoActualizado.getId());
    }
    
    @Test
    void testUpdate_AlumnoNoEncontrado() throws DuplicatedException {
        Alumno alumno = new Alumno("Mario", "Gomez", 11118888L);
        dao.saveAlumno(alumno);

        assertThrows(AlumnoNotFoundException.class, () -> {
            dao.update(22223333L, alumno);
        });
    }

}
