package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class AsignaturaTest {

    private static Materia materia;
    private static Profesor profesor;

    @BeforeAll
    public static void setUp(){
        profesor = new Profesor("Luciano", "Salotto", "Lic.");
        materia = new Materia("Laboratorio 3", 2, 1, profesor);
    }

    @Test
    public void testNewAsignatura() {
        Asignatura asignatura = new Asignatura(materia);
        assertEquals(EstadoAsignatura.NO_CURSADA, asignatura.getEstado());
        assertFalse(asignatura.getNota().isPresent());
        assertEquals("Laboratorio 3", asignatura.getNombreAsignatura());
    }

    @Test
    public void testAprobarAasignatura(){
        Asignatura asignatura = new Asignatura(materia);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        try {
            asignatura.aprobarAsignatura(8);
            assertEquals(EstadoAsignatura.APROBADA,asignatura.getEstado());
        } catch (EstadoIncorrectoException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test(expected = EstadoIncorrectoException.class)
//    public void testAprobarAsignaturaMateriaNoCursada() throws EstadoIncorrectoException{
//        Asignatura asignatura = new Asignatura(materia);
//        asignatura.aprobarAsignatura(8);
//    }
//
//    @Test (expected = EstadoIncorrectoException.class)
//    public void testAprobarAasignaturaYaAprobada() throws EstadoIncorrectoException{
//        Asignatura asignatura = new Asignatura(materia);
//        asignatura.cursarAsignatura();
//        asignatura.aprobarAsignatura(8);
//        assertEquals(EstadoAsignatura.APROBADA,asignatura.getEstado());
//        asignatura.aprobarAsignatura(9);
//
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void aprobarAsignaturaNotaMenorCero() throws EstadoIncorrectoException {
//        Asignatura asignatura = new Asignatura(materia);
//        asignatura.cursarAsignatura();
//        asignatura.aprobarAsignatura(-3);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void aprobarAsignaturaNotaMayorDiez() throws EstadoIncorrectoException {
//        Asignatura asignatura = new Asignatura(materia);
//        asignatura.cursarAsignatura();
//        asignatura.aprobarAsignatura(13);
//    }

    @Test
    public void aprobarAsignaturaNotaDesaprobado() throws EstadoIncorrectoException {
        Asignatura asignatura = new Asignatura(materia);
        asignatura.cursarAsignatura();
        asignatura.aprobarAsignatura(3);
        assertEquals(EstadoAsignatura.CURSADA, asignatura.getEstado());
    }
}