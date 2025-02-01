package ar.edu.utn.frbb.tup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorWithoutMateriasException;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {

    @Autowired
    ProfesorService profesorService;

    @PostMapping("/")
    public ResponseEntity<Profesor> crearProfesor(@RequestBody ProfesorDto profesorDto)
            throws DatoInvalidoException, DuplicatedException {
        Profesor profesor = profesorService.crearProfesor(profesorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profesor);
    }

    @GetMapping
    public ResponseEntity<Object> buscarProfesor(@RequestParam(required = false) Long idProfesor,
            @RequestParam(required = false) String apellido) throws ProfesorNotFoundException {
        if (apellido != null && !apellido.isBlank()) {
            List<Profesor> profesores = profesorService.buscarProfesorApellido(apellido);
            if (profesores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron profesores con el apellido proporcionado.");
            }
            return ResponseEntity.ok(profesores);
        }

        if (idProfesor != null) {
            Profesor profesor = profesorService.buscarProfesorPorId(idProfesor);
            return ResponseEntity.ok(profesor);
        }

        List<Profesor> profesores = profesorService.obtenerProfesores();
        if (profesores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{idProfesor}/materias")
public ResponseEntity<List<MateriaDto>> listarMateriasDeProfesor(@PathVariable Long idProfesor) throws ProfesorNotFoundException, ProfesorWithoutMateriasException {

        List<MateriaDto> materias = profesorService.obtenerMateriasPorProfesorDto(idProfesor);
        if (materias.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(materias);
}

    @PutMapping("/{idProfesor}")
    public ResponseEntity<Profesor> actualizarProfesorPorId(@PathVariable("idProfesor") Long idProfesor,
            @RequestBody ProfesorDto profesorDto) throws ProfesorNotFoundException, DatoInvalidoException {
        Profesor profesorActualizado = profesorService.actualizarProfesorPorId(idProfesor, profesorDto);
        return ResponseEntity.ok(profesorActualizado);
    }

    @DeleteMapping("/{idProfesor}")
    public ResponseEntity<Profesor> borrarProfesorPorId(@PathVariable("idProfesor") Long id)
            throws ProfesorNotFoundException, ProfesorWithoutMateriasException {
        profesorService.borrarProfesorPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
