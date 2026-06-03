package org.example.serveremulator.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GroupRequest {
    //Аннотация говорит, что строка не может быть null или пустой
    @NotBlank(message = "Название группы не может быть пустым")
    //Прямо задали длинну названия
    @Size(min = 5, max = 50, message = "Название группы должно содержать от 5 до 50 символов")
    private String name;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
