package ar.edu.utn.frbb.tup.model.dto;

import java.util.List;

public class AlumnoDto {
    private String nombre;
    private String apellido;
    private long dni;
    private List<Long> asignaturasId;

    public List<Long> getAsignaturasId() {
        return asignaturasId;
    }

    public void setAsignaturasId(List<Long> asignaturasId) {
        this.asignaturasId = asignaturasId;
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
}
