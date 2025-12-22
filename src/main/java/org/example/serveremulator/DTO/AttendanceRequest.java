package org.example.serveremulator.DTO;

import java.util.List;

public class AttendanceRequest {
    private Long lessonId;
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
