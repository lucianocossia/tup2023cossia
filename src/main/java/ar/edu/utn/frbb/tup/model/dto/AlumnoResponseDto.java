package ar.edu.utn.frbb.tup.model.dto;

import java.util.List;

public class AlumnoResponseDto {
     private long id;
    private String nombre;
    private String apellido;
    private long dni;
    private List<AsignaturaResponseDto> asignaturas;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public long getDni() {
        return dni;
    }
    public void setDni(long dni) {
        this.dni = dni;
    }
    public List<AsignaturaResponseDto> getAsignaturas() {
        return asignaturas;
    }
    public void setAsignaturas(List<AsignaturaResponseDto> asignaturas) {
        this.asignaturas = asignaturas;
    }

    
}
