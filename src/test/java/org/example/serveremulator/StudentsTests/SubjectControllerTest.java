package org.example.serveremulator.StudentsTests;

import org.example.serveremulator.Controllers.SubjectController;
import org.example.serveremulator.DTO.SubjectResponse;
import org.example.serveremulator.Entityes.Subject;
import org.example.serveremulator.Mappers.SubjectMapper;
import org.example.serveremulator.Security.JwtRequestFilter;
import org.example.serveremulator.Services.SubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectService subjectService;

    @MockitoBean
    private SubjectMapper subjectMapper;

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllSubjects_ReturnsOk() throws Exception {
        Subject subject = new Subject();
        SubjectResponse responseDto = new SubjectResponse();

        when(subjectService.getAllSubjects()).thenReturn(List.of(subject));
        when(subjectMapper.toResponse(any(Subject.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSubject_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}