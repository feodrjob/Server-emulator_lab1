package org.example.serveremulator.Services;

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Attendance;
import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.AttendanceRepository;
import org.example.serveremulator.Repositories.LessonRepository;
import org.example.serveremulator.Repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             LessonRepository lessonRepository,
                             StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
    }

    public Attendance getAttendanceByLessonId(Long lessonId) {
        if (lessonId == null || lessonId <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson ID must be positive number"
            );
        }

        return attendanceRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.ATTENDANCE_NOT_FOUND,
                        "Attendance not found for lesson id: " + lessonId
                ));
    }

    public Attendance createAttendance(Long lessonId, List<Long> presentStudentIds) {
        if (lessonId == null || lessonId <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson ID must be positive number"
            );
        }
        if (presentStudentIds == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Student IDs cannot be null"
            );
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.LESSON_NOT_FOUND,
                        "Lesson not found with id: " + lessonId
                ));

        if (attendanceRepository.findByLessonId(lessonId).isPresent()) {
            throw new ValidationException(
                    ErrorCode.ATTENDANCE_ALREADY_EXISTS,
                    "Attendance already exists for lesson id: " + lessonId
            );
        }

        // Получаем студентов и проверяем что они из нужной группы
        Set<Student> students = presentStudentIds.stream()
                .map(studentId -> studentRepository.findById(studentId)
                        .orElseThrow(() -> new NotFoundException(
                                ErrorCode.STUDENT_NOT_FOUND,
                                "Student not found with id: " + studentId
                        )))
                .collect(Collectors.toSet());

        for (Student student : students) {
            if (!student.getGroup().getId().equals(lesson.getGroup().getId())) {
                throw new ValidationException(
                        ErrorCode.STUDENT_NOT_IN_GROUP,
                        "Student " + student.getId() + " is not from lesson's group"
                );
            }
        }

        Attendance attendance = new Attendance(lesson);
        attendance.setPresentStudents(students);

        return attendanceRepository.save(attendance);
    }

    public Attendance updateAttendance(Long lessonId, List<Long> presentStudentIds) {
        if (lessonId == null || lessonId <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson ID must be positive number"
            );
        }
        if (presentStudentIds == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Student IDs cannot be null"
            );
        }

        Attendance attendance = attendanceRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.ATTENDANCE_NOT_FOUND,
                        "Attendance not found for lesson id: " + lessonId
                ));

        Lesson lesson = attendance.getLesson();

        Set<Student> students = presentStudentIds.stream()
                .map(studentId -> studentRepository.findById(studentId)
                        .orElseThrow(() -> new NotFoundException(
                                ErrorCode.STUDENT_NOT_FOUND,
                                "Student not found with id: " + studentId
                        )))
                .collect(Collectors.toSet());

        for (Student student : students) {
            if (!student.getGroup().getId().equals(lesson.getGroup().getId())) {
                throw new ValidationException(
                        ErrorCode.STUDENT_NOT_IN_GROUP,
                        "Student " + student.getId() + " is not from lesson's group"
                );
            }
        }

        attendance.setPresentStudents(students);

        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long lessonId) {
        if (lessonId == null || lessonId <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson ID must be positive number"
            );
        }

        if (!attendanceRepository.existsByLessonId(lessonId)) {
            throw new NotFoundException(
                    ErrorCode.ATTENDANCE_NOT_FOUND,
                    "Attendance not found for lesson id: " + lessonId
            );
        }

        attendanceRepository.deleteByLessonId(lessonId);
    }
}