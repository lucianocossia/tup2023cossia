package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.EstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlumnoTest {

    @Mock
    private Asignatura asignaturaMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Alumno crearAlumno(String nombre, String apellido, long dni) {
        return new Alumno(nombre, apellido, dni);
    }

    private Asignatura crearAsignaturaConEstado(Materia mat, long id, EstadoAsignatura estado) {
        Asignatura asig = new Asignatura(mat, id);
        asig.setEstado(estado);
        return asig;
    }

    // =========================================================================
    // TESTS QUE USAN MOCKS
    // =========================================================================

    /**
     * Escenario básico: el Alumno tiene en su lista la Asignatura,
     * no hay correlatividades y aprobamos con nota >=4.
     * Verificamos que se llame a asignatura.aprobarAsignatura(...) y que no lance
     * excepción.
     */
    @Test
    void testAprobarAsignatura_Exito() throws EstadoIncorrectoException, NotaException,
            CorrelatividadException, AsignaturaNotFoundException {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678);
        alumno.agregarAsignatura(asignaturaMock);

        // Mock: sin correlatividades y estado CURSADA
        when(asignaturaMock.getCorrelatividades()).thenReturn(Collections.emptyList());
        when(asignaturaMock.getEstado()).thenReturn(EstadoAsignatura.CURSADA);

        // Cuando: aprobamos con nota 7
        alumno.aprobarAsignatura(asignaturaMock, Optional.of(7));

        // Entonces: se llama al método en el mock con la nota 7
        verify(asignaturaMock).aprobarAsignatura(Optional.of(7));
    }

    /**
     * Escenario donde la Asignatura no está en la lista del Alumno:
     * esperamos un AsignaturaNotFoundException.
     */
    @Test
    void testAprobarAsignatura_AsignaturaNoEncontrada() {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678);
        // No agregamos la asignaturaMock al alumno

        // Esperamos un AsignaturaNotFoundException
        assertThrows(AsignaturaNotFoundException.class,
                () -> alumno.aprobarAsignatura(asignaturaMock, Optional.of(8)));
    }

    /**
     * Escenario en el que la Asignatura tiene correlativas NO aprobadas.
     * Debe lanzar CorrelatividadException al querer cursar o aprobar.
     */
    @Test
    void testCursarAsignatura_CorrelatividadNoAprobada() {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678);
        alumno.agregarAsignatura(asignaturaMock);

        // Mock: 1 correlativa (Materia)
        Materia correlativa = mock(Materia.class);
        when(correlativa.getNombre()).thenReturn("ProgramacionI");
        when(asignaturaMock.getCorrelatividades()).thenReturn(Collections.singletonList(correlativa));

        // Alumno no tiene la Asignatura de esa materia correlativa aprobada
        assertThrows(CorrelatividadException.class,
                () -> alumno.cursarAsignatura(asignaturaMock));
    }

    /**
     * Test donde verificamos que el estado de la Asignatura cambie a CURSADA
     * (usando el mock para ver que se invoque el método correspondiente).
     */
    @Test
    void testCursarAsignatura_Exito()
            throws AsignaturaNotFoundException, CorrelatividadException, EstadoAsignaturaException {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);
        alumno.agregarAsignatura(asignaturaMock);

        // Mock: sin correlatividades
        when(asignaturaMock.getCorrelatividades()).thenReturn(Collections.emptyList());

        // Cuando: cursamos asignatura
        alumno.cursarAsignatura(asignaturaMock);

        // Entonces: se llama al método en el mock
        verify(asignaturaMock, times(1)).cursarAsignatura();
    }

    // =========================================================================
    // TESTS CON OBJETOS REALES
    // =========================================================================

    /**
     * Escenario en el que la Asignatura está en NO_CURSADA y se llama
     * aprobarAsignatura(nota).
     * Debe lanzar EstadoIncorrectoException porque primero debe estar cursada.
     */
    @Test
    void testAprobarAsignatura_EstadoIncorrecto() {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);

        Materia materia = new Materia("Materia X", 1, 1, null);
        Asignatura asignaturaReal = crearAsignaturaConEstado(materia, 101L, EstadoAsignatura.NO_CURSADA);

        alumno.agregarAsignatura(asignaturaReal);

        // Esperamos que lance EstadoIncorrectoException
        assertThrows(EstadoIncorrectoException.class, () -> {
            alumno.aprobarAsignatura(asignaturaReal, Optional.of(7));
        });
    }

    /**
     * Escenario en el que se lanza NotaException por nota fuera de rango.
     */
    @Test
    void testAprobarAsignatura_NotaInvalida()
            throws CorrelatividadException, AsignaturaNotFoundException, EstadoIncorrectoException {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);

        Profesor profesor = new Profesor("Mario", "Rossi", "Dr.");
        Materia mat = new Materia("Matemática", 1, 1, profesor);
        Asignatura asignaturaReal = crearAsignaturaConEstado(mat, 101L, EstadoAsignatura.CURSADA);

        alumno.agregarAsignatura(asignaturaReal);

        // Esperamos que lance NotaException con nota 11 (fuera de 1..10)
        assertThrows(NotaException.class, () -> {
            alumno.aprobarAsignatura(asignaturaReal, Optional.of(11));
        });
    }

    /**
     * Escenario: Se aprueba con nota = 3 (baja), no lanza excepción,
     * pero no cambia a estado APROBADA.
     */
    @Test
    void testAprobarAsignatura_NotaBajaNoAprueba()
            throws EstadoIncorrectoException, NotaException, CorrelatividadException, AsignaturaNotFoundException {

        Alumno alumno = crearAlumno("Maria", "Lopez", 44444444);
        Materia mat = new Materia("Física", 1, 1, null);
        Asignatura asign = crearAsignaturaConEstado(mat, 200, EstadoAsignatura.CURSADA);

        alumno.agregarAsignatura(asign);

        // Cuando: intenta aprobar con nota 3
        alumno.aprobarAsignatura(asign, Optional.of(3));

        // Entonces: no lanza excepción, pero sigue en estado CURSADA
        assertEquals(EstadoAsignatura.CURSADA, asign.getEstado());
        assertEquals(Optional.of(3), asign.getNota());
    }

    /**
     * Escenario: aprobamos asignatura con Optional.empty() (sin nota).
     * Podría significar que no aprueba y no lanza excepción.
     */
    @Test
    void testAprobarAsignatura_SinNota()
            throws EstadoIncorrectoException, NotaException, CorrelatividadException, AsignaturaNotFoundException {

        Alumno alumno = crearAlumno("Maria", "Lopez", 44444444L);
        Materia mat = new Materia("Física", 1, 1, null);
        Asignatura asign = crearAsignaturaConEstado(mat, 200L, EstadoAsignatura.CURSADA);

        alumno.agregarAsignatura(asign);

        // Cuando: aprobarAsignatura(asign, Optional.empty())
        alumno.aprobarAsignatura(asign, Optional.empty());

        // Entonces: sigue CURSADA, la nota es Optional.empty()
        assertEquals(EstadoAsignatura.CURSADA, asign.getEstado());
        assertTrue(asign.getNota().isEmpty());
    }

    /**
     * Escenario: Asignatura con correlativa ya aprobada, debería cursarse sin error.
     */
    @Test
    void testCursarAsignatura_CorrelativaYaAprobada()
            throws AsignaturaNotFoundException, CorrelatividadException, EstadoAsignaturaException {

        // Materias
        Materia fisica1 = new Materia("Física 1", 1, 1, null);
        Materia fisica2 = new Materia("Física 2", 2, 1, null);
        fisica2.agregarCorrelatividad(fisica1);

        // Asignaturas
        Asignatura asigF1 = crearAsignaturaConEstado(fisica1, 101L, EstadoAsignatura.APROBADA);
        Asignatura asigF2 = crearAsignaturaConEstado(fisica2, 102L, EstadoAsignatura.NO_CURSADA);

        // Alumno con ambas asignaturas, F1 aprobada
        Alumno alumno = crearAlumno("Pedro", "González", 12345678);
        alumno.agregarAsignatura(asigF1);
        alumno.agregarAsignatura(asigF2);

        // Cuando: cursamos F2, no debería lanzar excepción
        alumno.cursarAsignatura(asigF2);

        // Entonces: estado CURSADA
        assertEquals(EstadoAsignatura.CURSADA, asigF2.getEstado());
    }

    /**
     * Test para comprobar que al actualizar una Asignatura ya existente en el alumno,
     * cambia sus campos correctamente.
     */
    @Test
    void testActualizarAsignatura() {
        Alumno alumno = crearAlumno("Ana", "Martinez", 87654321);

        Materia mat1 = new Materia("Química", 2, 1, null);
        Materia mat2 = new Materia("Biología", 2, 1, null);

        Asignatura asigQ = crearAsignaturaConEstado(mat1, 10, EstadoAsignatura.CURSADA);
        Asignatura asigB = crearAsignaturaConEstado(mat2, 20, EstadoAsignatura.NO_CURSADA);

        alumno.agregarAsignatura(asigQ);
        alumno.agregarAsignatura(asigB);

        // Nueva Asignatura "clon" de Química con estado APROBADA y nota 8
        Asignatura asigQUpdate = crearAsignaturaConEstado(mat1, 30, EstadoAsignatura.APROBADA);
        asigQUpdate.setNota(Optional.of(8));

        // Cuando: actualizamos
        alumno.actualizarAsignatura(asigQUpdate);

        // Entonces: la Asignatura "Química" dentro del alumno cambia a APROBADA y nota 8
        assertEquals(EstadoAsignatura.APROBADA, asigQ.getEstado());
        assertEquals(Optional.of(8), asigQ.getNota());

        // Y "Biología" no cambia
        assertEquals(EstadoAsignatura.NO_CURSADA, asigB.getEstado());
    }

}
