package org.example.serveremulator.StudentsTests;
import org.example.serveremulator.Controllers.GroupController;
import org.example.serveremulator.DTO.GroupRequest;
import org.example.serveremulator.DTO.GroupResponse;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Mappers.GroupMapper;
import org.example.serveremulator.Security.JwtRequestFilter;
import org.example.serveremulator.Services.GroupService;
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

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private GroupMapper groupMapper;

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllGroups_ReturnsOk() throws Exception {
        Group group = new Group();
        GroupResponse responseDto = new GroupResponse();
        responseDto.setName("ИС-20");

        when(groupService.getAllGroups()).thenReturn(List.of(group));
        when(groupMapper.toResponse(any(Group.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroup_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}