package ar.edu.utn.frbb.tup.model;




import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private long id;

    private String nombre;
    private String apellido;
    private long dni;

    private List<Asignatura> asignaturas;

    public Alumno() {
    }
    public Alumno(String nombre, String apellido, long dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;

        asignaturas = new ArrayList<>();

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public long getDni() {
        return dni;
    }

    public void agregarAsignatura(Asignatura a){
        this.asignaturas.add(a);
    }

    public List<Asignatura> obtenerListaAsignaturas(){
        return this.asignaturas;
    }

    public void aprobarAsignatura(Materia materia, int nota) throws EstadoIncorrectoException, CorrelatividadException, AsignaturaInexistenteException {
        Asignatura asignaturaAAprobar = getAsignaturaAAprobar(materia);

        for (Materia correlativa :
                materia.getCorrelatividades()) {
            chequearCorrelatividad(correlativa);
        }
        asignaturaAAprobar.aprobarAsignatura(nota);
    }

    private void chequearCorrelatividad(Materia correlativa) throws CorrelatividadException {
        for (Asignatura a:
                asignaturas) {
            if (correlativa.getNombre().equals(a.getNombreAsignatura())) {
                if (!EstadoAsignatura.APROBADA.equals(a.getEstado())) {
                    throw new CorrelatividadException("La asignatura " + a.getNombreAsignatura() + " no está aprobada");
                }
            }
        }
    }

    private Asignatura getAsignaturaAAprobar(Materia materia) throws AsignaturaInexistenteException {

        for (Asignatura a: asignaturas) {
            if (materia.getNombre().equals(a.getNombreAsignatura())) {
                return a;
            }
        }
        throw new AsignaturaInexistenteException("No se encontró la materia");
    }

    public boolean puedeAprobar(Asignatura asignatura) {
        return true;
    }

    public void actualizarAsignatura(Asignatura asignatura) {
        for (Asignatura a:
             asignaturas) {
            if (a.getNombreAsignatura().equals(asignatura.getNombreAsignatura())) {
                a.setEstado(asignatura.getEstado());
                a.setNota(asignatura.getNota().get());
            }
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
