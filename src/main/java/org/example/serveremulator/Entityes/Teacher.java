package org.example.serveremulator.Entityes;

import jakarta.persistence.*;

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
    private String midleName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    public Teacher() {}

    public Teacher(String lastName, String midleName, String firstName) {
        this.lastName = lastName;
        this.midleName = midleName;
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMidleName() {
        return midleName;
    }

    public void setMidleName(String midleName) {
        this.midleName = midleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(lastName, teacher.lastName) && Objects.equals(midleName, teacher.midleName) && Objects.equals(firstName, teacher.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, midleName, firstName);
    }
}
