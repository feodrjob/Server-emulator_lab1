package org.example.serveremulator.DTO;


import java.util.List;

public class AttendanceResponse {
    private Long id;
    private String lessonInfo;
    private List<String> presentStudents;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonInfo() {
        return lessonInfo;
    }

    public void setLessonInfo(String lessonInfo) {
        this.lessonInfo = lessonInfo;
    }

    public List<String> getPresentStudents() {
        return presentStudents;
    }

    public void setPresentStudents(List<String> presentStudents) {
        this.presentStudents = presentStudents;
    }
}