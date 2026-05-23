package org.example.serveremulator.Mappers;

import org.example.serveremulator.DTO.StudentRequest;
import org.example.serveremulator.DTO.StudentResponse;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Enums.StudentEnum;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Repositories.GroupRepository;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    private final GroupRepository groupRepository;

    public StudentMapper(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Student toEntity(StudentRequest request) {
        Student student = new Student();

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setMiddleName(request.getMiddleName());
        student.setStatus(request.getStatus());

        if (request.getGroupId() != null) {
            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new NotFoundException(
                            ErrorCode.GROUP_NOT_FOUND,
                            "Group with id " + request.getGroupId() + " not found"
                    ));
            student.setGroup(group);
        }

        return student;
    }

    public StudentResponse toResponse(Student student) {
        StudentResponse response = new StudentResponse();

        response.setId(student.getId());

        String fullName = student.getLastName() + " " + student.getFirstName();
        if (student.getMiddleName() != null && !student.getMiddleName().isEmpty()) {
            fullName += " " + student.getMiddleName();
        }
        response.setFullName(fullName);

        response.setGroupName(student.getGroup().getName());
        response.setStatus(student.getStatus().name());

        return response;
    }
}