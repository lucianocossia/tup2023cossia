package ar.edu.utn.frbb.tup.utils;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import org.springframework.stereotype.Component;

@Component
public class MateriaMapper {

    // Entidad -> DTO
    public MateriaDto toDto(Materia materia) {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre(materia.getNombre());
        materiaDto.setAnio(materia.getAnio());
        materiaDto.setCuatrimestre(materia.getCuatrimestre());

        if (materia.getProfesor() != null) {
            materiaDto.setProfesorId(materia.getProfesor().getId());
        }

        if (materia.getCorrelatividades() != null) {
            int[] correlatividadesId = materia.getCorrelatividades().stream()
                    .mapToInt(Materia::getMateriaId).toArray();
            materiaDto.setCorrelatividades(correlatividadesId);
        }

        return materiaDto;
    }

    public Materia toEntity(MateriaDto dto, Profesor profesor) {
        Materia materia = new Materia();
        materia.setNombre(dto.getNombre());
        materia.setAnio(dto.getAnio());
        materia.setCuatrimestre(dto.getCuatrimestre());

        materia.setProfesor(profesor);

        return materia;
    }
}

