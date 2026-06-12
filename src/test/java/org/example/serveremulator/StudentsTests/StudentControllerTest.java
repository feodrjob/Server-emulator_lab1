package org.example.serveremulator.StudentsTests;

import org.example.serveremulator.Controllers.StudentController;
import org.example.serveremulator.DTO.StudentResponse;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Mappers.StudentMapper;
import org.example.serveremulator.Security.JwtRequestFilter;
import org.example.serveremulator.Services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Заменили @MockBean на @MockitoBean
    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private StudentMapper studentMapper;

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllStudents_ReturnsOk() throws Exception {
        Student student = new Student();
        student.setId(1L);

        StudentResponse responseDto = new StudentResponse();
        responseDto.setFullName("Иванов Иван Иванович");

        when(studentService.getAllStudents()).thenReturn(List.of(student));
        when(studentMapper.toResponse(any(Student.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                // ПРОВЕРЯЕМ fullName вместо firstName
                .andExpect(jsonPath("$.data[0].fullName").value("Иванов Иван Иванович"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentById_ReturnsOk() throws Exception {
        Student student = new Student();
        StudentResponse responseDto = new StudentResponse();
        responseDto.setId(1L);

        when(studentService.getStudentById(1L)).thenReturn(student);
        when(studentMapper.toResponse(student)).thenReturn(responseDto);

        mockMvc.perform(get("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStudent_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}