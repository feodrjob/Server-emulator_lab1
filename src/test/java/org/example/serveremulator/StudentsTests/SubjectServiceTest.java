package org.example.serveremulator.StudentsTests;

import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Entityes.Subject;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.SubjectRepository;
import org.example.serveremulator.Services.SubjectService;
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
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject testSubject;

    @BeforeEach
    void setUp() {
        testSubject = new Subject();
        testSubject.setId(1L);
        testSubject.setName("Математика");
    }

    @Test
    void getSubjectById_Success() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(testSubject));

        Subject result = subjectService.getSubjectById(1L);

        assertNotNull(result);
        assertEquals("Математика", result.getName());
    }

    @Test
    void createSubject_Success() {
        when(subjectRepository.existsByName("Математика")).thenReturn(false);
        when(subjectRepository.save(any(Subject.class))).thenReturn(testSubject);

        Subject result = subjectService.createSubject(testSubject);

        assertNotNull(result);
        assertEquals("Математика", result.getName());
        verify(subjectRepository, times(1)).save(testSubject);
    }

    @Test
    void deleteSubject_HasLessons_ThrowsException() {
        Lesson lesson = new Lesson();
        testSubject.addLesson(lesson);

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(testSubject));

        assertThrows(ValidationException.class, () -> subjectService.deleteSubject(1L));
        verify(subjectRepository, never()).delete(any(Subject.class));
    }
}