package org.example.serveremulator.Mappers;

import org.example.serveremulator.DTO.LessonRequest;
import org.example.serveremulator.DTO.LessonResponse;
import org.example.serveremulator.Entityes.*;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public Lesson toEntity(LessonRequest request) {
        Lesson lesson = new Lesson();

        lesson.setDate(request.getDate());
        lesson.setLessonNumber(request.getLessonNumber());

        Teacher teacher = new Teacher();
        teacher.setId(request.getTeacherId());
        lesson.setTeacher(teacher);

        Subject subject = new Subject();
        subject.setId(request.getSubjectId());
        lesson.setSubject(subject);

        Group group = new Group();
        group.setId(request.getGroupId());
        lesson.setGroup(group);

        return lesson;
    }

    public LessonResponse toResponse(Lesson lesson) {
        LessonResponse response = new LessonResponse();

        response.setId(lesson.getId());
        response.setDate(lesson.getDate());
        response.setLessonNumber(lesson.getLessonNumber());

        String teacherFullName = lesson.getTeacher().getLastName() + " " +
                lesson.getTeacher().getFirstName() + " " +
                lesson.getTeacher().getMidleName();
        response.setTeacherName(teacherFullName);

        response.setSubjectName(lesson.getSubject().getName());

        response.setGroupName(lesson.getGroup().getName());

        return response;
    }
}