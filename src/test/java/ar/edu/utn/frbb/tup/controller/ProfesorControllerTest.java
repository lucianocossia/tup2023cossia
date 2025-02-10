package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfesorController.class)
public class ProfesorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfesorService profesorService;

    @MockBean
    private ar.edu.utn.frbb.tup.utils.MateriaMapper materiaMapper;

    @Test
    void testCrearProfesor_OK() throws Exception {

        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setTitulo("Lic.");

        Profesor profesor = new Profesor("Juan", "Perez", "Lic.");
        profesor.setId(1L);
        when(profesorService.crearProfesor(ArgumentMatchers.any(ProfesorDto.class))).thenReturn(profesor);

        mockMvc.perform(post("/profesor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Perez")))
                .andExpect(jsonPath("$.titulo", is("Lic.")));
    }

    @Test
    void testBuscarProfesor_ById_OK() throws Exception {
        Profesor profesor = new Profesor("Maria", "Lopez", "Dr.");
        profesor.setId(2L);
        when(profesorService.buscarProfesorPorId(2L)).thenReturn(profesor);

        mockMvc.perform(get("/profesor")
                .param("idProfesor", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido", is("Lopez")));
    }

    @Test
    void testBuscarProfesor_ByApellido_OK() throws Exception {
        Profesor profesor = new Profesor("Laura", "Ramirez", "Lic.");
        profesor.setId(3L);
        List<Profesor> lista = new ArrayList<>();
        lista.add(profesor);
        when(profesorService.buscarProfesorApellido("Ramirez")).thenReturn(lista);

        mockMvc.perform(get("/profesor")
                .param("apellido", "Ramirez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].apellido", is("Ramirez")));
    }

    @Test
    void testBuscarProfesor_NoParametros_DevuelveNoContent() throws Exception {
        mockMvc.perform(get("/profesor"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListarMateriasDeProfesor_OK() throws Exception {
        List<Materia> materias = new ArrayList<>();
        Materia materia = new Materia("Matemáticas", 2023, 1, null);
        materias.add(materia);

        when(profesorService.obtenerMateriasPorProfesor(1L)).thenReturn(materias);

        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Matemáticas");
        when(materiaMapper.toDto(ArgumentMatchers.<Materia>any())).thenReturn(materiaDto);

        mockMvc.perform(get("/profesor/1/materias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre", is("Matemáticas")));
    }

    @Test
    void testActualizarProfesorPorId() throws Exception {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Carlos Modificado");
        dto.setApellido("Martinez");
        dto.setTitulo("Dr.");

        Profesor profesor = new Profesor("Carlos Modificado", "Martinez", "Dr.");
        profesor.setId(4L);
        when(profesorService.actualizarProfesorPorId(eq(4L), ArgumentMatchers.<ProfesorDto>any())).thenReturn(profesor);

        mockMvc.perform(put("/profesor/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos Modificado")))
                .andExpect(jsonPath("$.apellido", is("Martinez")))
                .andExpect(jsonPath("$.titulo", is("Dr.")));
    }

    @Test
    void testBorrarProfesorPorId() throws Exception {
        Mockito.doNothing().when(profesorService).borrarProfesorPorId(5L);

        mockMvc.perform(delete("/profesor/5"))
                .andExpect(status().isNoContent());
    }
}
