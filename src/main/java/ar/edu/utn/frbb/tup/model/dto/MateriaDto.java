package ar.edu.utn.frbb.tup.model.dto;

import org.springframework.stereotype.Component;

@Component
public class MateriaDto {
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private Long profesorId;
    private Long [] correlatividades;

    public Long getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Long profesorId) {
        this.profesorId = profesorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Long[] getCorrelatividades() {
        return correlatividades;
    }

    public void setCorrelatividades(Long[] correlatividades) {
        this.correlatividades = correlatividades;
    }

}
