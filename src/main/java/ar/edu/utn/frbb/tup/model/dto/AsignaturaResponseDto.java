package ar.edu.utn.frbb.tup.model.dto;

import java.util.List;
import java.util.Optional;

public class AsignaturaResponseDto {
    private long asignaturaId;
    private String nombreAsignatura;
    private String estado;
    private Optional<Integer> nota;
    private long materia;
    private List<Long> correlatividades;
    
    public long getAsignaturaId() {
        return asignaturaId;
    }
    public void setAsignaturaId(long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }
    public String getNombreAsignatura() {
        return nombreAsignatura;
    }
    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Optional<Integer> getNota() {
        return nota;
    }
    public void setNota(Optional<Integer> nota) {
        this.nota = nota;
    }
    public long getMateria() {
        return materia;
    }
    public void setMateria(long materia) {
        this.materia = materia;
    }
    public List<Long> getCorrelatividades() {
        return correlatividades;
    }
    public void setCorrelatividades(List<Long> correlatividades) {
        this.correlatividades = correlatividades;
    }

    
}
