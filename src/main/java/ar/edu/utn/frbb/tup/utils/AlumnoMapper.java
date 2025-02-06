package ar.edu.utn.frbb.tup.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoResponseDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaResponseDto;

@Component
public class AlumnoMapper {

    // Entidad Alumno -> DTO de Respuesta
    public AlumnoResponseDto toResponseDto(Alumno alumno) {
        AlumnoResponseDto dto = new AlumnoResponseDto();
        dto.setId(alumno.getId());
        dto.setNombre(alumno.getNombre());
        dto.setApellido(alumno.getApellido());
        dto.setDni(alumno.getDni());

        if (alumno.getAsignaturas() != null) {
            List<AsignaturaResponseDto> asignaturasDto = alumno.getAsignaturas().stream()
                    .map(this::toAsignaturaResponseDto)
                    .collect(Collectors.toList());
            dto.setAsignaturas(asignaturasDto);
        }

        return dto;
    }

    // Asignatura -> AsignaturaResponseDto
    private AsignaturaResponseDto toAsignaturaResponseDto(Asignatura asignatura) {
        AsignaturaResponseDto dto = new AsignaturaResponseDto();
        dto.setAsignaturaId(asignatura.getAsignaturaId());
        dto.setNombreAsignatura(asignatura.getNombreAsignatura());

        dto.setEstado(asignatura.getEstado() != null ? asignatura.getEstado().name() : null);
        dto.setNota(asignatura.getNota());

        if (asignatura.getMateria() != null) {
            dto.setMateria(asignatura.getMateria().getMateriaId());
        }

        dto.setCorrelatividades(
                asignatura.getCorrelatividades().stream()
                        .map(Materia::getMateriaId)
                        .collect(Collectors.toList()));

        return dto;
    }

}
