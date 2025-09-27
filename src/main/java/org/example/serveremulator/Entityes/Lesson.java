package org.example.serveremulator.Entityes;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "lesson_number")
    private Integer lessonNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "teacher_id")
    private Teacher teacher;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Lesson() {}

    public Lesson(LocalDate date, Integer lessonNumber, Teacher teacher, Subject subject, Group group) {
        this.date = date;
        this.lessonNumber = lessonNumber;
        this.teacher = teacher;
        this.subject = subject;
        this.group = group; // ✅ теперь правильно
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup( Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson1 = (Lesson) o;
        return Objects.equals(id, lesson1.id) && Objects.equals(date, lesson1.date) && Objects.equals(lessonNumber, lesson1.lessonNumber) && Objects.equals(teacher, lesson1.teacher) && Objects.equals(subject, lesson1.subject) && Objects.equals(group, lesson1.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, lessonNumber, teacher, subject, group);
    }
}
