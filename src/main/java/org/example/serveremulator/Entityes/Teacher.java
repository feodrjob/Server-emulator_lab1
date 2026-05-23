package org.example.serveremulator.Entityes;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "midle_name", nullable = false)
    private String middleName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    // Обратная связь с занятиями - каскадное удаление
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    public Teacher() {}

    public Teacher(String lastName, String midleName, String firstName) {
        this.lastName = lastName;
        this.middleName = midleName;
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMidleName() {
        return middleName;
    }

    public void setMidleName(String midleName) {
        this.middleName = midleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Геттер для связей
    public List<Lesson> getLessons() {
        return lessons;
    }

    // Методы для управления связями
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setTeacher(this);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
        lesson.setTeacher(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(lastName, teacher.lastName) && Objects.equals(middleName, teacher.middleName) && Objects.equals(firstName, teacher.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, middleName, firstName);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lessonsCount=" + lessons.size() +
                '}';
    }
}