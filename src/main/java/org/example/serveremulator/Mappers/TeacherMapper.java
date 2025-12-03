package org.example.serveremulator.Mappers;

import org.example.serveremulator.DTO.GroupResponse;
import org.example.serveremulator.DTO.TeacherRequest;
import org.example.serveremulator.DTO.TeacherResponse;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Entityes.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    public Teacher toEntity(TeacherRequest request){
        Teacher teacher = new Teacher();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setMidleName(request.getMiddleName());
        return teacher;
    }

    public TeacherResponse toResponse (Teacher teacher){
        TeacherResponse response = new TeacherResponse();
        response.setFullName(teacher.getFirstName() + " " + teacher.getLastName());
        return response;
    }
}
