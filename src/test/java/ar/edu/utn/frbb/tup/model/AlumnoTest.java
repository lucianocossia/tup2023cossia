package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlumnoTest {

    private static Alumno alumno;
    private static Materia m1;
    private static Materia m2;
    private static Materia m3;
    private static Materia m4;
    private static Profesor profesor1;
    private static Asignatura a1;
    private static Asignatura a2;
    private static Asignatura a3;
    private static Asignatura a4;


    @BeforeAll
    public static void setUp() {

        profesor1 = new Profesor("Luciano", "Salotto", "Lic.");
        m1 = new Materia("Laboratorio 1", 1, 1, profesor1);
        m2 = new Materia("Laboratorio 2", 1, 2, profesor1);
        m3 = new Materia("Laboratorio 3", 2, 1, profesor1);
        m4 = new Materia("Laboratorio 4", 2, 2, profesor1);
        m2.agregarCorrelatividad(m1);
        m3.agregarCorrelatividad(m1);
        m3.agregarCorrelatividad(m2);
        m4.agregarCorrelatividad(m1);
        m4.agregarCorrelatividad(m2);
        m4.agregarCorrelatividad(m3);
        a1 = new Asignatura(m1);
        a2 = new Asignatura(m2);
        a3 = new Asignatura(m3);
        a4 = new Asignatura(m4);

    }

    @Test
    public void testNewAlumno() {
        alumno = new Alumno("Stefano", "D'Annunzio", 42431228);
        assertEquals("Stefano", alumno.getNombre());
        assertEquals("D'Annunzio", alumno.getApellido());
        assertEquals(42431228, alumno.getDni());

    }

    @Test
    public void testNewAlumnoConAsignaturas() {
        alumno = new Alumno("Stefano", "D'Annunzio", 42431228);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        alumno.agregarAsignatura(a3);
        alumno.agregarAsignatura(a4);
        assertEquals(4, alumno.obtenerListaAsignaturas().size());
        assertEquals("Laboratorio 1", alumno.obtenerListaAsignaturas().get(0).getNombreAsignatura());
        assertEquals("Laboratorio 2", alumno.obtenerListaAsignaturas().get(1).getNombreAsignatura());
        assertEquals("Laboratorio 3", alumno.obtenerListaAsignaturas().get(2).getNombreAsignatura());
        assertEquals("Laboratorio 4", alumno.obtenerListaAsignaturas().get(3).getNombreAsignatura());

    }

    @Test
    public void testNewAlumnoCursandoAsignaturas(){
        alumno = new Alumno("Stefano", "D'Annunzio", 42431228);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        alumno.agregarAsignatura(a3);
        alumno.agregarAsignatura(a4);
        alumno.obtenerListaAsignaturas().get(0).cursarAsignatura();
        alumno.obtenerListaAsignaturas().get(1).cursarAsignatura();
        alumno.obtenerListaAsignaturas().get(2).cursarAsignatura();
        alumno.obtenerListaAsignaturas().get(3).cursarAsignatura();
        assertEquals(EstadoAsignatura.CURSADA, alumno.obtenerListaAsignaturas().get(0).getEstado());
        assertEquals(EstadoAsignatura.CURSADA, alumno.obtenerListaAsignaturas().get(1).getEstado());
        assertEquals(EstadoAsignatura.CURSADA, alumno.obtenerListaAsignaturas().get(2).getEstado());
        assertEquals(EstadoAsignatura.CURSADA, alumno.obtenerListaAsignaturas().get(3).getEstado());


    }

    @Test
    public void testNewAlumnoAprobandoAsignaturas() throws EstadoIncorrectoException {
        alumno = new Alumno("Stefano", "D'Annunzio", 42431228);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        alumno.agregarAsignatura(a3);
        alumno.agregarAsignatura(a4);
        alumno.obtenerListaAsignaturas().get(0).cursarAsignatura();
        alumno.obtenerListaAsignaturas().get(1).cursarAsignatura();
        alumno.obtenerListaAsignaturas().get(2).cursarAsignatura();
        alumno.obtenerListaAsignaturas().get(3).cursarAsignatura();
        if(alumno.puedeAprobar(alumno.obtenerListaAsignaturas().get(1))) {
            alumno.obtenerListaAsignaturas().get(1).aprobarAsignatura(8);
        }
        if(alumno.puedeAprobar(alumno.obtenerListaAsignaturas().get(2))) {
            alumno.obtenerListaAsignaturas().get(2).aprobarAsignatura(8);
        }
        if(alumno.puedeAprobar(alumno.obtenerListaAsignaturas().get(3))) {
            alumno.obtenerListaAsignaturas().get(3).aprobarAsignatura(8);
        }
        //assertEquals(EstadoAsignatura.APROBADA, alumno.obtenerListaAsignaturas().get(0).getEstado());
        assertEquals(EstadoAsignatura.APROBADA, alumno.obtenerListaAsignaturas().get(1).getEstado());
        assertEquals(EstadoAsignatura.APROBADA, alumno.obtenerListaAsignaturas().get(2).getEstado());
        assertEquals(EstadoAsignatura.APROBADA, alumno.obtenerListaAsignaturas().get(3).getEstado());
    }

//    @Test(expected = CorrelatividadesNoAprobadasException.class)
//    public void testAlumnoAprobandoSinCumplirCorrelativas() throws CorrelatividadesNoAprobadasException {
//        alumno = new Alumno("Stefano", "D'Annunzio", 42431228);
//        alumno.agregarAsignatura(a1);
//        alumno.agregarAsignatura(a2);
//        alumno.agregarAsignatura(a3);
//        alumno.agregarAsignatura(a4);
//
//        Materia m5 = new Materia("Programacion 3", 2, 1, profesor1);
//        m5.agregarCorrelatividad(m1);
//        m5.agregarCorrelatividad(m2);
//        m4.agregarCorrelatividad(m5);
//        Asignatura a5 = new Asignatura(m5);
//        alumno.agregarAsignatura(a5);
//
//        alumno.obtenerListaAsignaturas().get(0).cursarAsignatura();
//        alumno.obtenerListaAsignaturas().get(1).cursarAsignatura();
//        alumno.obtenerListaAsignaturas().get(2).cursarAsignatura();
//        alumno.obtenerListaAsignaturas().get(3).cursarAsignatura();
//        alumno.obtenerListaAsignaturas().get(4).cursarAsignatura();
//        try {
//            if(alumno.puedeAprobar(alumno.obtenerListaAsignaturas().get(0))) {
//                alumno.obtenerListaAsignaturas().get(0).aprobarAsignatura(8);
//            }
//            if(alumno.puedeAprobar(alumno.obtenerListaAsignaturas().get(1))) {
//                alumno.obtenerListaAsignaturas().get(1).aprobarAsignatura(8);
//            }
//            if(alumno.puedeAprobar(alumno.obtenerListaAsignaturas().get(2))) {
//                alumno.obtenerListaAsignaturas().get(2).aprobarAsignatura(8);
//            }
//            if(alumno.puedeAprobar(alumno.obtenerListaAsignaturas().get(3))) {
//                alumno.obtenerListaAsignaturas().get(3).aprobarAsignatura(8);
//            }
//        }
//         catch (EstadoIncorrectoException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}