package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AlumnoResponseDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.utils.AlumnoMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlumnoController.class)
class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlumnoService alumnoService;

    @MockBean
    private AlumnoMapper alumnoMapper;

    @Test
    void testCrearAlumno() throws Exception {

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan");
        alumnoDto.setApellido("Pérez");
        alumnoDto.setDni(12345678L);

        Alumno alumno = new Alumno("Juan", "Pérez", 12345678L);
        alumno.setAsignaturas(new ArrayList<>());
        AlumnoResponseDto responseDto = new AlumnoResponseDto();
        responseDto.setNombre("Juan");
        responseDto.setApellido("Pérez");
        responseDto.setDni(12345678L);

        when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenReturn(alumno);
        when(alumnoMapper.toResponseDto(any(Alumno.class))).thenReturn(responseDto);

        mockMvc.perform(post("/alumno/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alumnoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"))
                .andExpect(jsonPath("$.dni").value(12345678L));
    }

    @Test
    void testBuscarAlumno_ById() throws Exception {

        Alumno alumno = new Alumno("Maria", "Lopez", 99999999L);
        alumno.setId(1L);
        when(alumnoService.buscarAlumnoPorId(1L)).thenReturn(alumno);

        mockMvc.perform(get("/alumno")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Lopez"));
    }

    @Test
    void testBuscarAlumno_ByDni() throws Exception {

        Alumno alumno = new Alumno("Pedro", "Gomez", 11111111L);
        when(alumnoService.buscarAlumnoPorDni(11111111L)).thenReturn(alumno);

        mockMvc.perform(get("/alumno")
                .param("dni", "11111111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro"));
    }

    @Test
    void testBuscarAlumno_ByApellido() throws Exception {

        Alumno alumno = new Alumno("Laura", "Ramirez", 22222222L);
        when(alumnoService.buscarAlumno("Ramirez")).thenReturn(alumno);

        mockMvc.perform(get("/alumno")
                .param("apellido", "Ramirez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Ramirez"));
    }

    @Test
    void testBuscarAlumno_BadRequest() throws Exception {
        mockMvc.perform(get("/alumno"))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Debe especificar al menos uno de los siguientes parámetros: id, dni o apellido"));
    }

    @Test
    void testObtenerAsignaturasAlumnoPorId() throws Exception {

        Materia materia = new Materia("LaboratorioIII", 2, 1, null);

        Asignatura asignatura = new Asignatura(materia, 1L);

        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);

        when(alumnoService.obtenerAsignaturasAlumnoPorId(1L)).thenReturn(asignaturas);

        mockMvc.perform(get("/alumno/1/asignaturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(asignaturas.size()));
    }

    @Test
    void testObtenerAsignaturaAlumnoPorId() throws Exception {

        Materia materia = new Materia("LaboratorioIII", 2, 1, null);

        Asignatura asignatura = new Asignatura(materia, 10L);

        when(alumnoService.obtenerAsignaturaAlumnoPorId(1L, 10L)).thenReturn(asignatura);

        mockMvc.perform(get("/alumno/1/asignaturas/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asignaturaId").value(10));
    }

    @Test
    void testActualizarAlumno() throws Exception {
        AlumnoDto dto = new AlumnoDto();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setDni(87654321L);

        Alumno alumno = new Alumno("Juan", "Perez", 87654321L);
        alumno.setId(1L);
        when(alumnoService.actualizarAlumnoPorId(eq(1L), any(AlumnoDto.class))).thenReturn(alumno);

        mockMvc.perform(put("/alumno/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.dni").value(87654321L));
    }

    @Test
    void testActualizarEstadoAsignaturaPorId() throws Exception {
        Materia materia = new Materia("LaboratorioIII", 2, 1, null);
        Asignatura asignatura = new Asignatura(materia, 10L);

        when(alumnoService.actualizarEstadoAsignaturaPorID(eq(1L), eq(10L), any(AsignaturaDto.class)))
                .thenReturn(asignatura);

        AsignaturaDto dto = new AsignaturaDto();
        dto.setEstado(EstadoAsignatura.CURSADA);
        dto.setNota(null);

        mockMvc.perform(put("/alumno/1/asignaturas/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asignaturaId").value(10));
    }

}
