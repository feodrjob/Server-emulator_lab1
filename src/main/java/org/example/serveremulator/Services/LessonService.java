package org.example.serveremulator.Services;

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;


@Service
@Transactional
public class LessonService {
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final AttendanceRepository attendanceRepository;

    public LessonService(LessonRepository lessonRepository,
                         TeacherRepository teacherRepository,
                         SubjectRepository subjectRepository,
                         GroupRepository groupRepository,
                         AttendanceRepository attendanceRepository) {
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson getLessonById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson ID must be positive number"
            );
        }

        return lessonRepository.findWithDetailsById(id) // Используем новый метод
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.LESSON_NOT_FOUND,
                        "Lesson with id " + id + " not found"
                ));
    }

    public List<Lesson> getLessonsByTeacherId(Long teacherId, LocalDate startDate, LocalDate endDate) {
        if (teacherId == null || teacherId <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Teacher ID must be positive number"
            );
        }
        if (startDate == null || endDate == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Dates cannot be null"
            );
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Start date cannot be after end date"
            );
        }

        if (!teacherRepository.existsById(teacherId)) {
            throw new NotFoundException(
                    ErrorCode.TEACHER_NOT_FOUND,
                    "Teacher not found with id: " + teacherId
            );
        }

        return lessonRepository.findByTeacherIdAndDateBetween(teacherId, startDate, endDate);
    }

    public List<Lesson> getLessonsByGroupId(Long groupId, LocalDate startDate, LocalDate endDate) {
        if (groupId == null || groupId <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Group ID must be positive number"
            );
        }
        if (startDate == null || endDate == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Dates cannot be null"
            );
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Start date cannot be after end date"
            );
        }

        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(
                    ErrorCode.GROUP_NOT_FOUND,
                    "Group not found with id: " + groupId
            );
        }

        return lessonRepository.findByGroupIdAndDateBetween(groupId, startDate, endDate);
    }

    public Lesson createLesson(Lesson lesson) {
        if (lesson == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson cannot be null"
            );
        }

        if (lesson.getDate() == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson date is required"
            );
        }
        if (lesson.getLessonNumber() == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson number is required"
            );
        }
        if (lesson.getTeacher() == null || lesson.getTeacher().getId() == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Teacher is required"
            );
        }
        if (lesson.getSubject() == null || lesson.getSubject().getId() == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Subject is required"
            );
        }
        if (lesson.getGroup() == null || lesson.getGroup().getId() == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Group is required"
            );
        }

        Long teacherId = lesson.getTeacher().getId();
        Long subjectId = lesson.getSubject().getId();
        Long groupId = lesson.getGroup().getId();

        if (!teacherRepository.existsById(teacherId)) {
            throw new NotFoundException(
                    ErrorCode.TEACHER_NOT_FOUND,
                    "Teacher not found with id: " + teacherId
            );
        }
        if (!subjectRepository.existsById(subjectId)) {
            throw new NotFoundException(
                    ErrorCode.SUBJECT_NOT_FOUND,
                    "Subject not found with id: " + subjectId
            );
        }
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(
                    ErrorCode.GROUP_NOT_FOUND,
                    "Group not found with id: " + groupId
            );
        }

        if (lesson.getLessonNumber() < 1 || lesson.getLessonNumber() > 8) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson number must be between 1 and 8"
            );
        }

        if (lessonRepository.existsByGroupIdAndDateAndLessonNumber(
                groupId,
                lesson.getDate(),
                lesson.getLessonNumber())) {
            throw new ValidationException(
                    ErrorCode.LESSON_SCHEDULE_CONFLICT,
                    "Group already has lesson at this time"
            );
        }

        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(Long id, Lesson lesson) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson ID must be positive number"
            );
        }

        Lesson existingLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.LESSON_NOT_FOUND,
                        "Lesson with id " + id + " not found"
                ));

        if (lesson.getDate() != null) {
            existingLesson.setDate(lesson.getDate());
        }

        if (lesson.getLessonNumber() != null) {
            if (lesson.getLessonNumber() < 1 || lesson.getLessonNumber() > 8) {
                throw new ValidationException(
                        ErrorCode.VALIDATION_ERROR,
                        "Lesson number must be between 1 and 8"
                );
            }
            existingLesson.setLessonNumber(lesson.getLessonNumber());
        }

        if (lesson.getTeacher() != null && lesson.getTeacher().getId() != null) {
            Long teacherId = lesson.getTeacher().getId();
            if (!teacherRepository.existsById(teacherId)) {
                throw new NotFoundException(
                        ErrorCode.TEACHER_NOT_FOUND,
                        "Teacher not found with id: " + teacherId
                );
            }
            existingLesson.setTeacher(lesson.getTeacher());
        }

        if (lesson.getSubject() != null && lesson.getSubject().getId() != null) {
            Long subjectId = lesson.getSubject().getId();
            if (!subjectRepository.existsById(subjectId)) {
                throw new NotFoundException(
                        ErrorCode.SUBJECT_NOT_FOUND,
                        "Subject not found with id: " + subjectId
                );
            }
            existingLesson.setSubject(lesson.getSubject());
        }

        if (lesson.getGroup() != null && lesson.getGroup().getId() != null) {
            Long groupId = lesson.getGroup().getId();
            if (!groupRepository.existsById(groupId)) {
                throw new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Group not found with id: " + groupId
                );
            }
            existingLesson.setGroup(lesson.getGroup());
        }

        if (lessonRepository.existsByGroupIdAndDateAndLessonNumber(
                existingLesson.getGroup().getId(),
                existingLesson.getDate(),
                existingLesson.getLessonNumber())) {
            throw new ValidationException(
                    ErrorCode.LESSON_SCHEDULE_CONFLICT,
                    "Group already has lesson at this time"
            );
        }

        return lessonRepository.save(existingLesson);
    }

    public void deleteLesson(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Lesson ID must be positive number"
            );
        }

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.LESSON_NOT_FOUND,
                        "Lesson with id " + id + " not found"
                ));

        attendanceRepository.deleteByLessonId(id);

        lessonRepository.delete(lesson);

    }
}