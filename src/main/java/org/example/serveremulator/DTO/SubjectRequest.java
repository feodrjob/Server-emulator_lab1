package org.example.serveremulator.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubjectRequest {
    @NotBlank(message = "Название предмета не может быть пустым")
    @Size(min = 2, max = 50, message = "Некорректная длинна названия")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
