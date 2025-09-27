package org.example.serveremulator.Services;

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Student;
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

    public Optional<Student> getStudentById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Student ID must be positive number");
        }
        return studentRepository.findById(id);
    }

    public List<Student> getStudentsByGroupId(Long groupId) {
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID must be positive number");
        }
        return studentRepository.findByGroupId(groupId);
    }

    public Student createStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (student.getGroup() == null) {
            throw new IllegalArgumentException("Group is required");
        }

        if (!groupRepository.existsById(student.getGroup().getId())) {
            throw new IllegalArgumentException("Group not found with id: " + student.getGroup().getId());
        }

        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Student ID must be positive number");
        }

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        if (studentDetails.getFirstName() != null && !studentDetails.getFirstName().trim().isEmpty()) {
            existingStudent.setFirstName(studentDetails.getFirstName());
        }
        if (studentDetails.getMiddleName() != null) {
            existingStudent.setMiddleName(studentDetails.getMiddleName());
        }
        if (studentDetails.getLastName() != null && !studentDetails.getLastName().trim().isEmpty()) {
            existingStudent.setLastName(studentDetails.getLastName());
        }
        if (studentDetails.getStatus() != null) {
            existingStudent.setStatus(studentDetails.getStatus());
        }
        if (studentDetails.getGroup() != null) {
            if (!groupRepository.existsById(studentDetails.getGroup().getId())) {
                throw new IllegalArgumentException("Group not found with id: " + studentDetails.getGroup().getId());
            }
            existingStudent.setGroup(studentDetails.getGroup());
        }

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Student ID must be positive number");
        }

        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }
}