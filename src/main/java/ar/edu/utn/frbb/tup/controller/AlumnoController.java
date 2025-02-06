package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AlumnoResponseDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.EstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;
import ar.edu.utn.frbb.tup.utils.AlumnoMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AlumnoMapper alumnoMapper;

     @PostMapping("/")
    public ResponseEntity<AlumnoResponseDto> crearAlumno(@RequestBody AlumnoDto alumnoDto)
            throws DatoInvalidoException, DuplicatedException {

        Alumno alumnoCreado = alumnoService.crearAlumno(alumnoDto);        
        AlumnoResponseDto response = alumnoMapper.toResponseDto(alumnoCreado);        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Object> buscarAlumno(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Long dni) throws AlumnoNotFoundException {

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
                        "Debe especificar al menos uno de los siguientes parámetros: id, dni o apellido");
            }
    }

    @GetMapping("/{idAlumno}/asignaturas")
    public List<Asignatura> obtenerAsignaturasAlumnoPorId(@PathVariable("idAlumno") Long id) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoService.obtenerAsignaturasAlumnoPorId(id);
    }

    @GetMapping("/{idAlumno}/asignaturas/{idAsignatura}")
    public Asignatura obtenerAsignaturaAlumnoPorId(@PathVariable("idAlumno") Long id,
                                                   @PathVariable ("idAsignatura") Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoService.obtenerAsignaturaAlumnoPorId(id, idAsignatura);
    }

    @PutMapping("/{idAlumno}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable Long idAlumno, @RequestBody AlumnoDto alumnoDto) throws AlumnoNotFoundException {
        Alumno alumno = alumnoService.actualizarAlumnoPorId(idAlumno, alumnoDto);
        return ResponseEntity.ok(alumno);
    }

     @PutMapping("/{idAlumno}/asignaturas/{idAsignatura}")
    public Asignatura actualizarEstadoAsignaturaPorId(@PathVariable("idAlumno") Long idAlumno,
                                                  @PathVariable("idAsignatura") Long idAsignatura,
                                                  @RequestBody AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException,
            CorrelatividadException, EstadoIncorrectoException, NotaException, EstadoAsignaturaException, DuplicatedException {
        return alumnoService.actualizarEstadoAsignaturaPorID(idAlumno, idAsignatura, asignaturaDto);
    }

}
