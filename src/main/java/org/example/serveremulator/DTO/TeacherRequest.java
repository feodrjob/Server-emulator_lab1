package org.example.serveremulator.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TeacherRequest {

    @NotBlank(message = "Поле имя не может быть пустым")
    @Size(min = 2, max = 20, message = "Неккоретная длина")
    private String firstName;
    @NotBlank(message = "Поле фамилия не может быть пустым")
    @Size(min = 2, max = 20, message = "Неккоретная длина")
    private String lastName;
    @NotBlank(message = "Поле отчество не может быть пустым")
    @Size(min = 2, max = 20, message = "Неккоретная длина")
    private String middleName;

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getMiddleName() {return middleName;}
    public void setMiddleName(String middleName) {this.middleName = middleName;}


}
