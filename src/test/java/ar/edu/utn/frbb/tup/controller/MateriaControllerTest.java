package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.utils.MateriaMapper;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MateriaController.class)
public class MateriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MateriaService materiaService;

    @MockBean
    private MateriaMapper materiaMapper;

    @Test
    void testCrearMateria_OK() throws Exception {

        MateriaDto inputDto = new MateriaDto();
        inputDto.setNombre("LaboratorioIII");
        inputDto.setAnio(2);
        inputDto.setCuatrimestre(1);
        inputDto.setProfesorId(100L);
        inputDto.setCorrelatividades(new Long[]{});

        Materia materiaCreada = new Materia();
        materiaCreada.setNombre("LaboratorioIII");
        materiaCreada.setAnio(2);
        materiaCreada.setCuatrimestre(1);

        when(materiaService.crearMateria(ArgumentMatchers.any(MateriaDto.class)))
                .thenReturn(materiaCreada);

        MateriaDto responseDto = new MateriaDto();
        responseDto.setNombre("LaboratorioIII");
        responseDto.setAnio(2);
        responseDto.setCuatrimestre(1);
        responseDto.setProfesorId(100L);
        responseDto.setCorrelatividades(new Long[]{});
        when(materiaMapper.toDto(Mockito.<Materia>any())).thenReturn(responseDto);

        mockMvc.perform(post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("LaboratorioIII")))
                .andExpect(jsonPath("$.anio", is(2)))
                .andExpect(jsonPath("$.cuatrimestre", is(1)))
                .andExpect(jsonPath("$.profesorId", is(100)));
    }

    @Test
    void testBuscarMateria_ById_OK() throws Exception {

        Materia materia = new Materia();
        materia.setNombre("LaboratorioIII");
        when(materiaService.buscarMateriaPorId(1L)).thenReturn(materia);

        mockMvc.perform(get("/materia")
                .param("idMateria", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("LaboratorioIII")));
    }

    @Test
    void testBuscarMateria_ByNombre_OK() throws Exception {
        List<Materia> lista = new ArrayList<>();
        Materia m1 = new Materia();
        m1.setNombre("LaboratorioII");
        Materia m2 = new Materia();
        m2.setNombre("LaboratorioIII");
        lista.add(m1);
        lista.add(m2);
        when(materiaService.buscarMateriaPorNombre("Lab")).thenReturn(lista);

        mockMvc.perform(get("/materia")
                .param("nombre", "Lab"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("LaboratorioII")))
                .andExpect(jsonPath("$[1].nombre", is("LaboratorioIII")));
    }

    @Test
    void testBuscarMateria_ByNombre_NotFound() throws Exception {
        
        when(materiaService.buscarMateriaPorNombre("Base de Datos")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/materia")
                .param("nombre", "Base de Datos"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron materias con el nombre proporcionado."));
    }

    @Test
    void testObtenerMaterias() throws Exception {
        List<Materia> lista = new ArrayList<>();
        lista.add(new Materia("ProgramacionI", 1, 1, null));
        lista.add(new Materia("LaboratorioII", 1, 2, null));
        when(materiaService.obtenerMaterias()).thenReturn(lista);

        mockMvc.perform(get("/materia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testObtenerMaterias_Empty() throws Exception {
        when(materiaService.obtenerMaterias()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/materia"))
                .andExpect(status().isNoContent());
    }
}
