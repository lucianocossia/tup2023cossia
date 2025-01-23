package ar.edu.utn.frbb.tup.model;

import java.util.List;

public class Carrera {

    private final String nombre;
    private int cantidadAnios;
    private List<Materia> materiasList;

    public Carrera(String nombre, int cantidadAnios) {
        this.nombre = nombre;
        this.cantidadAnios = cantidadAnios;
    }

    public void agregarMateria(Materia materia) {
    }
}
