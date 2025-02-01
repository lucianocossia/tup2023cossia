package ar.edu.utn.frbb.tup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.utils.MateriaMapper;

@RestController
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private MateriaMapper materiaMapper;

    @PostMapping
    public ResponseEntity<MateriaDto> crearMateria(@RequestBody MateriaDto materiaDto) {
        try {
            Materia materiaCreada = materiaService.crearMateria(materiaDto);

            // Convertir la materia creada a DTO antes de responder
            MateriaDto salida = materiaMapper.toDto(materiaCreada);

            return ResponseEntity.status(HttpStatus.CREATED).body(salida);

        } catch (ProfesorNotFoundException | DuplicatedException | MateriaNotFoundException e) {
            // Manejo de errores
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping
    public ResponseEntity<Object> buscarMateria(@RequestParam(required = false) Integer idMateria,
            @RequestParam(required = false) String nombre) throws MateriaNotFoundException, DatoInvalidoException {

        if (idMateria != null) {
            Materia materia = materiaService.buscarMateriaPorId(idMateria);
            return ResponseEntity.ok(materia);
        }

        if (nombre != null && !nombre.isBlank()) {
            List<Materia> materias = materiaService.buscarMateriaPorNombre(nombre);
            if (materias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron materias con el nombre proporcionado.");
            }
            return ResponseEntity.ok(materias);
        }

        List<Materia> materias = materiaService.obtenerMaterias();
        if (materias.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(materias);
    }

}
