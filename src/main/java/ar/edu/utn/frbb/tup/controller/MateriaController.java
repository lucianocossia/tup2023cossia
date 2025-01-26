package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public Materia crearMateria(@RequestBody MateriaDto materiaDto) throws ProfesorNotFoundException, MateriaNotFoundException, DuplicatedException {
        return materiaService.crearMateria(materiaDto);
    }

    @GetMapping("/{idMateria}")
    public Materia buscarMateriaPorID(@PathVariable("idMateria") Integer id) throws MateriaNotFoundException {
        return materiaService.buscarMateriaPorId(id);
    }

    @GetMapping
    public List<Materia> buscarMateriaPorNombre(@RequestParam String nombre) throws MateriaNotFoundException {
        return materiaService.buscarMateriaPorNombre(nombre);
    }
}
