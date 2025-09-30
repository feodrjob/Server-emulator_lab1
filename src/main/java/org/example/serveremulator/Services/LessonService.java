package org.example.serveremulator.Services;

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Repositories.GroupRepository;
import org.example.serveremulator.Repositories.LessonRepository;
import org.example.serveremulator.Repositories.SubjectRepository;
import org.example.serveremulator.Repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonService {
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;

    public LessonService(LessonRepository lessonRepository,
                         TeacherRepository teacherRepository,
                         SubjectRepository subjectRepository,
                         GroupRepository groupRepository) {
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getLessonById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Lesson ID cannot be null or negative");
        }
        return lessonRepository.findById(id);
    }

    public List<Lesson> getLessonsByTeacherId(Long teacherId, LocalDate startDate, LocalDate endDate) {
        if (teacherId == null || teacherId <= 0) {
            throw new IllegalArgumentException("Teacher ID cannot be null or negative");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (!teacherRepository.existsById(teacherId)) {
            throw new IllegalArgumentException("Teacher not found with id: " + teacherId);
        }

        return lessonRepository.findByTeacherIdAndDateBetween(teacherId, startDate, endDate);
    }

    public List<Lesson> getLessonsByGroupId(Long groupId, LocalDate startDate, LocalDate endDate) {
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID cannot be null or negative");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (!groupRepository.existsById(groupId)) {
            throw new IllegalArgumentException("Group not found with id: " + groupId);
        }

        return lessonRepository.findByGroupIdAndDateBetween(groupId, startDate, endDate);
    }

    public Lesson createLesson(Lesson lesson) {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson cannot be null");
        }

        if (lesson.getDate() == null) {
            throw new IllegalArgumentException("Lesson date is required");
        }
        if (lesson.getLessonNumber() == null) {
            throw new IllegalArgumentException("Lesson number is required");
        }
        if (lesson.getTeacher() == null || lesson.getTeacher().getId() == null) {
            throw new IllegalArgumentException("Teacher is required");
        }
        if (lesson.getSubject() == null || lesson.getSubject().getId() == null) {
            throw new IllegalArgumentException("Subject is required");
        }
        if (lesson.getGroup() == null || lesson.getGroup().getId() == null) {
            throw new IllegalArgumentException("Group is required");
        }

        if (!teacherRepository.existsById(lesson.getTeacher().getId())) {
            throw new IllegalArgumentException("Teacher not found with id: " + lesson.getTeacher().getId());
        }
        if (!subjectRepository.existsById(lesson.getSubject().getId())) {
            throw new IllegalArgumentException("Subject not found with id: " + lesson.getSubject().getId());
        }
        if (!groupRepository.existsById(lesson.getGroup().getId())) {
            throw new IllegalArgumentException("Group not found with id: " + lesson.getGroup().getId());
        }

        if (lesson.getLessonNumber() < 1 || lesson.getLessonNumber() > 8) {
            throw new IllegalArgumentException("Lesson number must be between 1 and 8");
        }

        if (lessonRepository.existsByGroupIdAndDateAndLessonNumber(
                lesson.getGroup().getId(),
                lesson.getDate(),
                lesson.getLessonNumber())) {
            throw new IllegalArgumentException("Group already has lesson at this time");
        }

        return lessonRepository.save(lesson);
    }

    // 6. Обновить занятие
    public Lesson updateLesson(Long id, Lesson lesson) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Lesson ID cannot be null or negative");
        }

        Lesson existingLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson with id " + id + " not found"));

        if (lesson.getDate() != null) {
            existingLesson.setDate(lesson.getDate());
        }

        if (lesson.getLessonNumber() != null) {
            if (lesson.getLessonNumber() < 1 || lesson.getLessonNumber() > 8) {
                throw new IllegalArgumentException("Lesson number must be between 1 and 8");
            }
            existingLesson.setLessonNumber(lesson.getLessonNumber());
        }

        if (lesson.getTeacher() != null && lesson.getTeacher().getId() != null) {
            if (!teacherRepository.existsById(lesson.getTeacher().getId())) {
                throw new IllegalArgumentException("Teacher not found with id: " + lesson.getTeacher().getId());
            }
            existingLesson.setTeacher(lesson.getTeacher());
        }

        if (lesson.getSubject() != null && lesson.getSubject().getId() != null) {
            if (!subjectRepository.existsById(lesson.getSubject().getId())) {
                throw new IllegalArgumentException("Subject not found with id: " + lesson.getSubject().getId());
            }
            existingLesson.setSubject(lesson.getSubject());
        }

        if (lesson.getGroup() != null && lesson.getGroup().getId() != null) {
            if (!groupRepository.existsById(lesson.getGroup().getId())) {
                throw new IllegalArgumentException("Group not found with id: " + lesson.getGroup().getId());
            }
            existingLesson.setGroup(lesson.getGroup());
        }

        if (lessonRepository.existsByGroupIdAndDateAndLessonNumber(
                existingLesson.getGroup().getId(),
                existingLesson.getDate(),
                existingLesson.getLessonNumber())) {
            throw new IllegalArgumentException("Group already has lesson at this time");
        }

        return lessonRepository.save(existingLesson);
    }
    public void deleteLesson(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Lesson ID cannot be null or negative");
        }

        if (!lessonRepository.existsById(id)) {
            throw new IllegalArgumentException("Lesson not found with id: " + id);
        }

        lessonRepository.deleteById(id);
    }
}