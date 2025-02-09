package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;

public class Carrera {

    private final String nombre;
    private int cantidadAnios;
    private List<Materia> materiasList;

    public Carrera(String nombre, int cantidadAnios) {
        this.nombre = nombre;
        this.cantidadAnios = cantidadAnios;
        this.materiasList = new ArrayList<>();
    }

    public void agregarMateria(Materia materia) {
        materiasList.add(materia);
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadAnios() {
        return cantidadAnios;
    }

    public List<Materia> getMateriasList() {
        return materiasList;
    }
}