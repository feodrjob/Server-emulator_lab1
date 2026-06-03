package org.example.serveremulator.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AttendanceRequest {
    @NotNull(message = "ID занятия не может быть пустым")
    private Long lessonId;
    @NotNull(message = "список студентов не может быть Null")
    private List<Long> presentStudentIds;

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public List<Long> getPresentStudentIds() {
        return presentStudentIds;
    }

    public void setPresentStudentIds(List<Long> presentStudentIds) {
        this.presentStudentIds = presentStudentIds;
    }
}
