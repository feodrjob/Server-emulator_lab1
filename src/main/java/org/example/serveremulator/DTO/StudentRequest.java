package org.example.serveremulator.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Enums.StudentEnum;

public class StudentRequest {
    @NotBlank(message = "Поле имя не может быть пустым")
    @Size(min = 2, max = 20, message = "Некорректная длина имени")
    private String firstName;
    @NotBlank(message = "Поле фамилия не может быть пустым")
    @Size(min = 2, max = 20, message = "Некорректная длина фамилии")
    private String lastName;
    @Size(max = 20, message = "Некорректная длина отчества")
    private String middleName;
    @NotNull(message = "ID группы не можеты быть пустым")
    private Long groupId;
    @NotNull(message = "Статус стужента не может быть пустым")
    private StudentEnum status;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public StudentEnum getStatus() {
        return status;
    }
    public void setStatus(StudentEnum status) {
        this.status = status;
    }
}