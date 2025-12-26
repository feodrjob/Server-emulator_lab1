package org.example.serveremulator.Mappers;

import org.example.serveremulator.DTO.LessonRequest;
import org.example.serveremulator.DTO.LessonResponse;
import org.example.serveremulator.Entityes.*;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LessonMapper {
    private static final Logger logger = LoggerFactory.getLogger(LessonMapper.class);

    public Lesson toEntity(LessonRequest request) {
        logger.info("LessonMapper.toEntity() - Преобразование Request в Entity");
        logger.debug("Request data: date={}, lessonNumber={}, teacherId={}, subjectId={}, groupId={}",
                request.getDate(), request.getLessonNumber(),
                request.getTeacherId(), request.getSubjectId(), request.getGroupId());

        Lesson lesson = new Lesson();
        lesson.setDate(request.getDate());
        lesson.setLessonNumber(request.getLessonNumber());

        Teacher teacher = new Teacher();
        teacher.setId(request.getTeacherId());
        lesson.setTeacher(teacher);
        logger.debug("Создан Teacher proxy с id: {}", request.getTeacherId());

        Subject subject = new Subject();
        subject.setId(request.getSubjectId());
        lesson.setSubject(subject);
        logger.debug("Создан Subject proxy с id: {}", request.getSubjectId());

        Group group = new Group();
        group.setId(request.getGroupId());
        lesson.setGroup(group);
        logger.debug("Создан Group proxy с id: {}", request.getGroupId());

        return lesson;
    }

    public LessonResponse toResponse(Lesson lesson) {
        logger.info("LessonMapper.toResponse() - Преобразование Entity в Response");
        logger.debug("Lesson entity: id={}, date={}, lessonNumber={}",
                lesson.getId(), lesson.getDate(), lesson.getLessonNumber());

        LessonResponse response = new LessonResponse();
        response.setId(lesson.getId());
        response.setDate(lesson.getDate());
        response.setLessonNumber(lesson.getLessonNumber());

        // Безопасное получение teacher
        Teacher teacher = lesson.getTeacher();
        logger.debug("Teacher object: {}", teacher);
        if (teacher != null) {
            logger.debug("Teacher fields: id={}, firstName={}, lastName={}, middleName={}",
                    teacher.getId(), teacher.getFirstName(),
                    teacher.getLastName(), teacher.getMidleName());

            String middleName = teacher.getMidleName();
            String teacherFullName = teacher.getLastName() + " " +
                    teacher.getFirstName() + " " +
                    (middleName != null ? middleName : "");
            response.setTeacherName(teacherFullName.trim());
            logger.debug("Teacher name constructed: {}", teacherFullName.trim());
        } else {
            response.setTeacherName("Не указан");
            logger.warn("Teacher is null for lesson id: {}", lesson.getId());
        }

        // Безопасное получение subject
        Subject subject = lesson.getSubject();
        logger.debug("Subject object: {}", subject);
        if (subject != null) {
            response.setSubjectName(subject.getName());
            logger.debug("Subject name: {}", subject.getName());
        } else {
            response.setSubjectName("Не указана");
            logger.warn("Subject is null for lesson id: {}", lesson.getId());
        }

        // Безопасное получение group
        Group group = lesson.getGroup();
        logger.debug("Group object: {}", group);
        if (group != null) {
            response.setGroupName(group.getName());
            logger.debug("Group name: {}", group.getName());
        } else {
            response.setGroupName("Не указана");
            logger.warn("Group is null for lesson id: {}", lesson.getId());
        }

        logger.info("Response created: id={}, teacherName={}, subjectName={}, groupName={}",
                response.getId(), response.getTeacherName(),
                response.getSubjectName(), response.getGroupName());

        return response;
    }
}