package org.example.serveremulator.Entityes;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "attendance ")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;


    @ManyToMany
    @JoinTable(
            name = "Attendance_students",
            joinColumns = @JoinColumn (name = "Attendance_id"),
            inverseJoinColumns = @JoinColumn (name = "students_id")
    )
    private Set<Student> presentStudents = new HashSet<>();

    public Attendance() {}

    public Attendance(Lesson lesson) {
        this.lesson = lesson;
    }

    public Long getId() {
        return id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Set<Student> getPresentStudents() {
        return presentStudents;
    }

    public void setPresentStudents(Set<Student> presentStudents) {
        this.presentStudents = presentStudents;
    }
    public void addPresentStudent(Student  student) {
        presentStudents.add(student);
    }
    public void removePresentStudent(Student student) {
        presentStudents.remove(student);
    }
    public boolean isStudentPresent(Student student) {
        return presentStudents.contains(student);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(id, that.id) && Objects.equals(lesson, that.lesson) && Objects.equals(presentStudents, that.presentStudents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lesson, presentStudents);
    }
}
