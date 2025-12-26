package org.example.serveremulator.Services;

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.GroupRepository;
import org.example.serveremulator.Repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Student ID must be positive number"
            );
        }

        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.STUDENT_NOT_FOUND,
                        "Student with id " + id + " not found"
                ));
    }

    public List<Student> getStudentsByGroupId(Long groupId) {
        if (groupId == null || groupId <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Group ID must be positive number"
            );
        }

        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(
                    ErrorCode.GROUP_NOT_FOUND,
                    "Group with id " + groupId + " not found"
            );
        }

        return studentRepository.findByGroupId(groupId);
    }

    public Student createStudent(Student student) {
        if (student == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Student cannot be null"
            );
        }

        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.STUDENT_INVALID_NAME,
                    "First name is required"
            );
        }
        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.STUDENT_INVALID_NAME,
                    "Last name is required"
            );
        }

        if (student.getGroup() == null || student.getGroup().getId() == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Group is required"
            );
        }

        Long groupId = student.getGroup().getId();
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(
                    ErrorCode.GROUP_NOT_FOUND,
                    "Group with id " + groupId + " not found"
            );
        }

        student.setFirstName(student.getFirstName().trim());
        student.setLastName(student.getLastName().trim());
        if (student.getMiddleName() != null) {
            student.setMiddleName(student.getMiddleName().trim());
        }

        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Student ID must be positive number"
            );
        }

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.STUDENT_NOT_FOUND,
                        "Student with id " + id + " not found"
                ));

        if (studentDetails.getFirstName() != null && !studentDetails.getFirstName().trim().isEmpty()) {
            existingStudent.setFirstName(studentDetails.getFirstName().trim());
        }

        if (studentDetails.getMiddleName() != null) {
            existingStudent.setMiddleName(studentDetails.getMiddleName().trim());
        }

        if (studentDetails.getLastName() != null && !studentDetails.getLastName().trim().isEmpty()) {
            existingStudent.setLastName(studentDetails.getLastName().trim());
        }

        if (studentDetails.getStatus() != null) {
            existingStudent.setStatus(studentDetails.getStatus());
        }

        if (studentDetails.getGroup() != null && studentDetails.getGroup().getId() != null) {
            Long newGroupId = studentDetails.getGroup().getId();

            if (!groupRepository.existsById(newGroupId)) {
                throw new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Group with id " + newGroupId + " not found"
                );
            }

            if (!newGroupId.equals(existingStudent.getGroup().getId())) {
                existingStudent.setGroup(studentDetails.getGroup());
            }
        }

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Student ID must be positive number"
            );
        }

        if (!studentRepository.existsById(id)) {
            throw new NotFoundException(
                    ErrorCode.STUDENT_NOT_FOUND,
                    "Student with id " + id + " not found"
            );
        }

        studentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return studentRepository.existsById(id);
    }
}