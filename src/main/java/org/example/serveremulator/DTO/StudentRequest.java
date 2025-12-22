package org.example.serveremulator.DTO;

import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Enums.StudentEnum;

public class StudentRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private Group group;
    private StudentEnum status;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public StudentEnum getStatus() { return status; }
    public void setStatus(StudentEnum status) { this.status = status; }

}
