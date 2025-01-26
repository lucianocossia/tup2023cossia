package ar.edu.utn.frbb.tup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

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

    @PutMapping("/{idProfesor}")
    public Profesor actualizarProfesorPorId(@PathVariable("idProfesor") Long idProfesor,
                                          @RequestBody ProfesorDto profesorDto) throws ProfesorNotFoundException, DatoInvalidoException {
        return profesorService.actualizarProfesorPorId(idProfesor, profesorDto);
    }

}