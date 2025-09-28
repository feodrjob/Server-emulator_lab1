package org.example.serveremulator.Services;

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Subject;
import org.example.serveremulator.Repositories.SubjectRepository;
import org.example.serveremulator.Repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List <Subject> getAllSubjects()
    {
        return subjectRepository.findAll();
    }
    public Optional<Subject> getSubjectById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id is null");
        }
        return subjectRepository.findById(id);
    }

    public Subject createSubject(Subject subject) {
        if (subject == null || subject.getName() == null) {
            throw new IllegalArgumentException("name is null");
        }
         if (subjectRepository.existsByName(subject.getName())) {
             throw new IllegalArgumentException("name already exists");
         }
         return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, Subject subject) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id is null");
        }
        Subject updatedSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("subject not found"));
        if (subject.getName() != null && !subject.getName().trim().isEmpty()
                && !subject.getName().equals(updatedSubject.getName())) {

            if (subjectRepository.existsByName(subject.getName())) {
                throw new IllegalArgumentException("Subject with name '" + subject.getName() + "' already exists");
            }
            updatedSubject.setName(subject.getName());
        }
        return subjectRepository.save(updatedSubject);
    }
    public void deleteSubject(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id is null");
        }
        if  (!subjectRepository.existsById(id)) {
            throw new IllegalArgumentException("subject not found");
        }
        subjectRepository.deleteById(id);
    }
}

















