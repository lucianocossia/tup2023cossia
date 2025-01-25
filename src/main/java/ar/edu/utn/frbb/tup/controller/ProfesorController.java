package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("profesor")
public class ProfesorController {

    @Autowired
    ProfesorService profesorService;

    @PostMapping("/")
    public Profesor crearProfesor(@RequestBody ProfesorDto profesorDto) throws DatoInvalidoException, DuplicatedException {
        return profesorService.crearProfesor(profesorDto);
    }

    @GetMapping
    public List<Profesor> buscarProfesorPorApellido(@RequestParam String apellido) throws ProfesorNotFoundException {
        return profesorService.buscarProfesorApellido(apellido);
    }

    @GetMapping("/{idProfesor}")
    public Profesor buscarProfesorPorId(@PathVariable ("idProfesor") Long id) throws ProfesorNotFoundException {
        return profesorService.buscarProfesorPorId(id);
    }

}