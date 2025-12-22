package org.example.serveremulator.DTO;

import org.example.serveremulator.Enums.StudentEnum;

public class StudentResponse {
    private Long id;
    private String fullName;
    private String groupName;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}