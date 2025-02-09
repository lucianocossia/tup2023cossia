package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AsignaturaTest {

    private Materia materiaBase;

    @BeforeEach
    void setUp() {
        Profesor profesor = new Profesor("Luciano", "Salotto", "Lic.");
        materiaBase = new Materia("LaboratorioIII", 2, 1, profesor);
    }

    @Test
    void testConstructorSinParametros() {
        Asignatura asign = new Asignatura();

        assertNull(asign.getAsignaturaId());
        assertNull(asign.getMateria());
    }

    @Test
    void testConstructorConMateria() {
        Asignatura asign = new Asignatura(materiaBase, 100L);

        assertEquals(100, asign.getAsignaturaId());
        assertEquals(materiaBase, asign.getMateria());
        assertEquals(EstadoAsignatura.NO_CURSADA, asign.getEstado());
        assertTrue(asign.getNota().isEmpty());
    }

    @Test
    void testGetNombreAsignatura() {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        assertEquals("LaboratorioIII", asign.getNombreAsignatura());
    }

    @Test
    void testGetCorrelatividades() {
        Materia correlativa1 = new Materia("LaboratorioII", 1, 2, null);
        Materia correlativa2 = new Materia("LaboratorioIII", 2, 1, null);
        materiaBase.agregarCorrelatividad(correlativa1);
        materiaBase.agregarCorrelatividad(correlativa2);

        Asignatura asign = new Asignatura(materiaBase, 200L);
        assertEquals(2, asign.getCorrelatividades().size());
        assertEquals("LaboratorioII", asign.getCorrelatividades().get(0).getNombre());
        assertEquals("LaboratorioIII", asign.getCorrelatividades().get(1).getNombre());
    }

    @Test
    void testCursarAsignatura() {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        assertEquals(EstadoAsignatura.NO_CURSADA, asign.getEstado());

        asign.cursarAsignatura();
        assertEquals(EstadoAsignatura.CURSADA, asign.getEstado());
    }

    @Test
    void testAprobarAsignatura_EstadoIncorrecto() {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        asign.setEstado(EstadoAsignatura.NO_CURSADA);

        assertThrows(EstadoIncorrectoException.class, () -> {
            asign.aprobarAsignatura(Optional.of(7));
        });
    }

    @Test
    void testAprobarAsignatura_NotaInvalidaMenorACero() {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        asign.setEstado(EstadoAsignatura.CURSADA);

        assertThrows(NotaException.class, () -> {
            asign.aprobarAsignatura(Optional.of(-1));
        });
    }

    @Test
    void testAprobarAsignatura_NotaInvalidaMayorADiez() {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        asign.setEstado(EstadoAsignatura.CURSADA);

        assertThrows(NotaException.class, () -> {
            asign.aprobarAsignatura(Optional.of(11));
        });
    }

    @Test
    void testAprobarAsignatura_NotaAprobatoria() throws EstadoIncorrectoException, NotaException {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        asign.setEstado(EstadoAsignatura.CURSADA);

        asign.aprobarAsignatura(Optional.of(7));

        assertEquals(EstadoAsignatura.APROBADA, asign.getEstado());
        assertEquals(Optional.of(7), asign.getNota());
    }

    @Test
    void testAprobarAsignatura_NotaBajaNoAprueba() throws EstadoIncorrectoException, NotaException {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        asign.setEstado(EstadoAsignatura.CURSADA);

        asign.aprobarAsignatura(Optional.of(3));

        assertEquals(EstadoAsignatura.CURSADA, asign.getEstado());
        assertEquals(Optional.of(3), asign.getNota());
    }

    @Test
    void testAprobarAsignatura_SinNota() throws EstadoIncorrectoException, NotaException {
        Asignatura asign = new Asignatura(materiaBase, 101L);
        asign.setEstado(EstadoAsignatura.CURSADA);

        asign.aprobarAsignatura(Optional.empty());

        assertEquals(EstadoAsignatura.CURSADA, asign.getEstado());
        assertTrue(asign.getNota().isEmpty());
    }
}
