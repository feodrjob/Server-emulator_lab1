package org.example.serveremulator.Services;
import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Teacher;
import org.example.serveremulator.Repositories.StudentRepository;
import org.example.serveremulator.Repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id is null or id <= 0");
        }
        return teacherRepository.findById(id);
    }

    public Teacher createTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }

        if (teacher.getFirstName() == null || teacher.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (teacher.getLastName() == null || teacher.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        if (teacherRepository.existsByName(
                teacher.getLastName(),
                teacher.getFirstName(),
                teacher.getMidleName())) {
            throw new IllegalArgumentException("Teacher already exists");
        }
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(Long id, Teacher teacher) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id is null or id <= 0");
        }
        Teacher existingTeacher = teacherRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Teacher with id " + id + " not found"));
        if (teacher.getFirstName() != null || !teacher.getFirstName().trim().isEmpty()){
            existingTeacher.setFirstName(teacher.getFirstName());
        }
        if (teacher.getLastName() != null || !teacher.getLastName().trim().isEmpty()){
            existingTeacher.setLastName(teacher.getLastName());
        }
        if (teacher.getMidleName() != null || !teacher.getMidleName().trim().isEmpty()){
            existingTeacher.setMidleName(teacher.getMidleName());
        }
        return teacherRepository.save(existingTeacher);
    }

    public void deleteTeacher(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id is null or id <= 0");
        }
        if (!teacherRepository.existsById(id)) {
            throw new IllegalArgumentException("Teacher with id " + id + " not found");
        }
        teacherRepository.deleteById(id);
    }

}




