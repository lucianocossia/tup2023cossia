package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @PostMapping("/")
    public Alumno crearAlumno(@RequestBody AlumnoDto alumnoDto) throws DatoInvalidoException {
        return alumnoService.crearAlumno(alumnoDto);
    }

    @GetMapping
public ResponseEntity<Object> buscarAlumno(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) String apellido,
        @RequestParam(required = false) Long dni
) {
    try {
        if (id != null) {
            Alumno alumno = alumnoService.buscarAlumnoPorId(id);
            return ResponseEntity.ok(alumno);
        } else if (dni != null) {
            Alumno alumno = alumnoService.buscarAlumnoPorDni(dni);
            return ResponseEntity.ok(alumno);
        } else if (apellido != null && !apellido.isBlank()) {
            Alumno alumno = alumnoService.buscarAlumno(apellido);
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.badRequest().body(
                "Debe especificar al menos uno de los siguientes parámetros: id, dni o apellido"
            );
        }
    } catch (AlumnoNotFoundException e) {
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}

}
