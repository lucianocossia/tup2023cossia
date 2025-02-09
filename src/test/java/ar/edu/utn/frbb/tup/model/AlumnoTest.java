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

    @Test
    void testAprobarAsignatura_Exito() throws EstadoIncorrectoException, NotaException,
            CorrelatividadException, AsignaturaNotFoundException {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);
        alumno.agregarAsignatura(asignaturaMock);

        when(asignaturaMock.getCorrelatividades()).thenReturn(Collections.emptyList());
        when(asignaturaMock.getEstado()).thenReturn(EstadoAsignatura.CURSADA);

        alumno.aprobarAsignatura(asignaturaMock, Optional.of(7));

        verify(asignaturaMock).aprobarAsignatura(Optional.of(7));
    }

    @Test
    void testAprobarAsignatura_AsignaturaNoEncontrada() {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);
        assertThrows(AsignaturaNotFoundException.class,
                () -> alumno.aprobarAsignatura(asignaturaMock, Optional.of(8)));
    }

    @Test
    void testCursarAsignatura_CorrelatividadNoAprobada() {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);
        alumno.agregarAsignatura(asignaturaMock);

        Materia correlativa = mock(Materia.class);
        when(correlativa.getNombre()).thenReturn("ProgramacionI");
        when(asignaturaMock.getCorrelatividades()).thenReturn(Collections.singletonList(correlativa));

        assertThrows(CorrelatividadException.class,
                () -> alumno.cursarAsignatura(asignaturaMock));
    }

    @Test
    void testCursarAsignatura_Exito()
            throws AsignaturaNotFoundException, CorrelatividadException, EstadoAsignaturaException {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);
        alumno.agregarAsignatura(asignaturaMock);

        when(asignaturaMock.getCorrelatividades()).thenReturn(Collections.emptyList());

        alumno.cursarAsignatura(asignaturaMock);

        verify(asignaturaMock, times(1)).cursarAsignatura();
    }

    @Test
    void testAprobarAsignatura_EstadoIncorrecto() {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);

        Materia materia = new Materia("ProgramacionIII", 1, 1, null);
        Asignatura asignaturaReal = crearAsignaturaConEstado(materia, 101L, EstadoAsignatura.NO_CURSADA);

        alumno.agregarAsignatura(asignaturaReal);

        assertThrows(EstadoIncorrectoException.class, () -> {
            alumno.aprobarAsignatura(asignaturaReal, Optional.of(7));
        });
    }

    @Test
    void testAprobarAsignatura_NotaInvalida()
            throws CorrelatividadException, AsignaturaNotFoundException, EstadoIncorrectoException {
        Alumno alumno = crearAlumno("Juan", "Pérez", 12345678L);

        Profesor profesor = new Profesor("Luciano", "Salotto", "Lic.");
        Materia mat = new Materia("LaboratorioIII", 2, 1, profesor);
        Asignatura asignaturaReal = crearAsignaturaConEstado(mat, 101L, EstadoAsignatura.CURSADA);

        alumno.agregarAsignatura(asignaturaReal);

        assertThrows(NotaException.class, () -> {
            alumno.aprobarAsignatura(asignaturaReal, Optional.of(11));
        });
    }

    @Test
    void testAprobarAsignatura_NotaBajaNoAprueba()
            throws EstadoIncorrectoException, NotaException, CorrelatividadException, AsignaturaNotFoundException {

        Alumno alumno = crearAlumno("Maria", "Lopez", 44444444L);
        Materia mat = new Materia("LaboratorioIII", 2, 1, null);
        Asignatura asign = crearAsignaturaConEstado(mat, 200L, EstadoAsignatura.CURSADA);

        alumno.agregarAsignatura(asign);

        alumno.aprobarAsignatura(asign, Optional.of(3));

        assertEquals(EstadoAsignatura.CURSADA, asign.getEstado());
        assertEquals(Optional.of(3), asign.getNota());
    }

    @Test
    void testAprobarAsignatura_SinNota()
            throws EstadoIncorrectoException, NotaException, CorrelatividadException, AsignaturaNotFoundException {

        Alumno alumno = crearAlumno("Maria", "Lopez", 44444444L);
        Materia mat = new Materia("LaboratorioIII", 2, 1, null);
        Asignatura asign = crearAsignaturaConEstado(mat, 200L, EstadoAsignatura.CURSADA);

        alumno.agregarAsignatura(asign);

        alumno.aprobarAsignatura(asign, Optional.empty());

        assertEquals(EstadoAsignatura.CURSADA, asign.getEstado());
        assertTrue(asign.getNota().isEmpty());
    }

    @Test
    void testCursarAsignatura_CorrelativaYaAprobada()
            throws AsignaturaNotFoundException, CorrelatividadException, EstadoAsignaturaException {

        Materia laboratorioII = new Materia("LaboratorioII", 1, 2, null);
        Materia laboratorioIII = new Materia("LaboratorioIII", 2, 1, null);
        laboratorioIII.agregarCorrelatividad(laboratorioII);

        Asignatura asigLII = crearAsignaturaConEstado(laboratorioII, 101L, EstadoAsignatura.APROBADA);
        Asignatura asigLIII = crearAsignaturaConEstado(laboratorioIII, 102L, EstadoAsignatura.NO_CURSADA);

        Alumno alumno = crearAlumno("Pedro", "González", 12345678L);
        alumno.agregarAsignatura(asigLII);
        alumno.agregarAsignatura(asigLIII);

        alumno.cursarAsignatura(asigLIII);

        assertEquals(EstadoAsignatura.CURSADA, asigLIII.getEstado());
    }

    @Test
    void testActualizarAsignatura() {
        Alumno alumno = crearAlumno("Ana", "Martinez", 87654321L);

        Materia mat1 = new Materia("LaboratorioII", 1, 2, null);
        Materia mat2 = new Materia("LaboratorioIII", 2, 1, null);

        Asignatura asigQ = crearAsignaturaConEstado(mat1, 10L, EstadoAsignatura.CURSADA);
        Asignatura asigB = crearAsignaturaConEstado(mat2, 20L, EstadoAsignatura.NO_CURSADA);

        alumno.agregarAsignatura(asigQ);
        alumno.agregarAsignatura(asigB);

        Asignatura asigQUpdate = crearAsignaturaConEstado(mat1, 30L, EstadoAsignatura.APROBADA);
        asigQUpdate.setNota(Optional.of(8));

        alumno.actualizarAsignatura(asigQUpdate);

        assertEquals(EstadoAsignatura.APROBADA, asigQ.getEstado());
        assertEquals(Optional.of(8), asigQ.getNota());

        assertEquals(EstadoAsignatura.NO_CURSADA, asigB.getEstado());
    }

}
