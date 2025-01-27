package ar.edu.utn.frbb.tup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public Object buscarProfesor(@RequestParam(required = false) Long idProfesor, 
                                 @RequestParam(required = false) String apellido) throws ProfesorNotFoundException {
        if (apellido != null && !apellido.isBlank()) {
            return profesorService.buscarProfesorApellido(apellido);
        }
    
        if (idProfesor != null) {
            return profesorService.buscarProfesorPorId(idProfesor);
        }
        
        return profesorService.obtenerProfesores();
    }
    
    
    @PutMapping("/{idProfesor}")
    public Profesor actualizarProfesorPorId(@PathVariable("idProfesor") Long idProfesor,
                                          @RequestBody ProfesorDto profesorDto) throws ProfesorNotFoundException, DatoInvalidoException {
        return profesorService.actualizarProfesorPorId(idProfesor, profesorDto);
    }

    @DeleteMapping("/{idProfesor}")
    public void borrarProfesorPorId(@PathVariable("idProfesor") Long id) throws ProfesorNotFoundException {
        profesorService.borrarProfesorPorId(id);
    }

}