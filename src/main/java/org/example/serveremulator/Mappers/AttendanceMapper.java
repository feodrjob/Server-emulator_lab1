package org.example.serveremulator.Mappers;


import org.example.serveremulator.DTO.AttendanceRequest;
import org.example.serveremulator.DTO.AttendanceResponse;
import org.example.serveremulator.Entityes.Attendance;
import org.example.serveremulator.Entityes.Lesson;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttendanceMapper {

    public Attendance toEntity(AttendanceRequest request) {
        Attendance attendance = new Attendance();

        Lesson lesson = new Lesson();
        lesson.setId(request.getLessonId());
        attendance.setLesson(lesson);


        return attendance;
    }

    public AttendanceResponse toResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();

        response.setId(attendance.getId());

        Lesson lesson = attendance.getLesson();
        String lessonInfo = lesson.getSubject().getName() + " - " +
                lesson.getGroup().getName() + " - " +
                lesson.getDate().toString() + " (" +
                lesson.getLessonNumber() + " пара)";
        response.setLessonInfo(lessonInfo);

        List<String> studentNames = attendance.getPresentStudents().stream()
                .map(student -> student.getLastName() + " " + student.getFirstName())
                .collect(Collectors.toList());
        response.setPresentStudents(studentNames);

        return response;
    }
}