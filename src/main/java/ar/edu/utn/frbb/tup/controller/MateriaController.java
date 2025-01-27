package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public Materia crearMateria(@RequestBody MateriaDto materiaDto)
            throws ProfesorNotFoundException, MateriaNotFoundException, DuplicatedException {
        return materiaService.crearMateria(materiaDto);
    }

    @GetMapping
    public ResponseEntity<?> buscarMateria(@RequestParam(required = false) Integer idMateria,
            @RequestParam(required = false) String nombre) throws MateriaNotFoundException, DatoInvalidoException {
        if (idMateria != null) {
            Materia materia = materiaService.buscarMateriaPorId(idMateria);
            return ResponseEntity.ok(materia);
        }

        if (nombre != null && !nombre.isBlank()) {
            List<Materia> materias = materiaService.buscarMateriaPorNombre(nombre);
            return ResponseEntity.ok(materias);
        }

        throw new DatoInvalidoException("Debe proporcionar al menos un criterio de búsqueda: 'idMateria' o 'nombre'.");
    }

}
