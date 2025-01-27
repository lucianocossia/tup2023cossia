package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public ResponseEntity<Materia> crearMateria(@RequestBody MateriaDto materiaDto)
            throws ProfesorNotFoundException, MateriaNotFoundException, DuplicatedException {
        Materia materia = materiaService.crearMateria(materiaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(materia);
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
