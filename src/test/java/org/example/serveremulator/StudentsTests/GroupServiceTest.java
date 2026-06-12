package org.example.serveremulator.StudentsTests;

import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.GroupRepository;
import org.example.serveremulator.Repositories.StudentRepository;
import org.example.serveremulator.Services.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GroupService groupService;

    private Group testGroup;

    @BeforeEach
    void setUp() {
        testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setName("ИС-20");
    }

    @Test
    void getGroupById_Success() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));

        Group result = groupService.getGroupById(1L);

        assertNotNull(result);
        assertEquals("ИС-20", result.getName());
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    void createGroup_Success() {
        when(groupRepository.existsByName("ИС-20")).thenReturn(false);
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        Group result = groupService.createGroup(testGroup);

        assertNotNull(result);
        assertEquals("ИС-20", result.getName());
    }

    @Test
    void createGroup_AlreadyExists_ThrowsException() {
        when(groupRepository.existsByName("ИС-20")).thenReturn(true);

        assertThrows(ValidationException.class, () -> groupService.createGroup(testGroup));
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void deleteGroup_HasStudents_ThrowsException() {
        Student student = new Student();
        testGroup.addStudent(student);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));

        // Проверяем, что удаление заблокируется
        assertThrows(ValidationException.class, () -> groupService.deleteGroup(1L));
        verify(groupRepository, never()).delete(any(Group.class));
    }
}