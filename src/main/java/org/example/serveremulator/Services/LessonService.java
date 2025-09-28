package org.example.serveremulator.Services;

/*getAllLessons()

getLessonById(Long id)

getLessonsByTeacherId(Long teacherId, LocalDate startDate, LocalDate endDate)

getLessonsByGroupId(Long groupId, LocalDate startDate, LocalDate endDate)

createLesson(Lesson lesson)

updateLesson(Long id, Lesson lessonDetails)

deleteLesson(Long id)

Особые проверки:

Существование Teacher, Subject, Group

Корректность даты и номера пары (1-8)

Проверка на конфликты расписания*/

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Repositories.LessonRepository;
import org.example.serveremulator.Repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Lesson id must be greater than zero");
        }
        return lessonRepository.findById(id);
    }

}























