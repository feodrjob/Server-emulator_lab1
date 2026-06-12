package org.example.serveremulator.StudentsTests;

import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.AttendanceRepository;
import org.example.serveremulator.Repositories.GroupRepository;
import org.example.serveremulator.Repositories.StudentRepository;
import org.example.serveremulator.Services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Говорим, что будем использовать Mockito для подделки зависимостей
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    // Подделываем репозитории (нам не нужна реальная БД для тестов сервиса)
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private AttendanceRepository attendanceRepository;

    // Внедряем наши подделки в реальный StudentService
    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private Group testGroup;

    // Этот метод запускается ПЕРЕД каждым тестом, чтобы подготовить данные
    @BeforeEach
    void setUp() {
        testGroup = new Group();
        testGroup.setId(1L);

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("Иван");
        testStudent.setLastName("Иванов");
        testStudent.setGroup(testGroup);
    }

    @Test
    void getStudentById_Success() {
        // Учим наш поддельный репозиторий: когда вызовут findById(1), верни testStudent
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        Student result = studentService.getStudentById(1L);

        // Проверяем, что результат тот, что мы ожидали
        assertNotNull(result);
        assertEquals("Иван", result.getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getStudentById_NotFound_ThrowsException() {
        // Учим репозиторий возвращать пустоту
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Проверяем, что при попытке найти несуществующего студента вылетает НАША ошибка
        assertThrows(NotFoundException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    void createStudent_Success() {
        // Учим репозиторий групп говорить, что группа существует
        when(groupRepository.existsById(1L)).thenReturn(true);
        // Учим репозиторий студентов возвращать сохраненного студента
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student result = studentService.createStudent(testStudent);

        assertNotNull(result);
        assertEquals("Иван", result.getFirstName());
        verify(studentRepository, times(1)).save(testStudent); // проверяем, что save был вызван 1 раз
    }

    @Test
    void createStudent_GroupNotFound_ThrowsException() {
        when(groupRepository.existsById(1L)).thenReturn(false); // Группы нет!

        assertThrows(NotFoundException.class, () -> studentService.createStudent(testStudent));
        verify(studentRepository, never()).save(any(Student.class)); // Убеждаемся, что до сохранения не дошло
    }

    @Test
    void deleteStudent_HasAttendance_ThrowsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        // Эмулируем, что студент есть в журнале посещаемости (ты сам писал эту логику!)
        when(attendanceRepository.isStudentPresentInAnyAttendance(testStudent)).thenReturn(true);

        // Проверяем, что вылетает ValidationException из-за связей в БД
        ValidationException exception = assertThrows(ValidationException.class, () -> studentService.deleteStudent(1L));
        assertTrue(exception.getMessage().contains("is already deleted") || exception.getMessage().contains("conflict"));
    }
}