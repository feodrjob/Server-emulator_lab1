package org.example.serveremulator.DTO;
import java.time.LocalDate;

public class LessonRequest {
    private LocalDate date;          // Дата занятия
    private Integer lessonNumber;    // Номер пары (1-8)
    private Long teacherId;          // ID преподавателя
    private Long subjectId;          // ID дисциплины
    private Long groupId;            // ID группы

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}