package org.example.serveremulator.DTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class LessonRequest {

    @NotNull(message = "Дата не может быть пустой")
    private LocalDate date;

    @NotNull(message = "Номер пары не может быть пустым")
    @Min(value = 1, message = "Номер пары не может быть меньше 1")
    @Max(value = 8, message = "Номер пары не может быть больше 8")
    private Integer lessonNumber;

    @NotNull(message = "ID Учителя не может быть пустым")
    @Min(value = 1, message = "Некорректный ID Учителя (должен быть > 0)")
    private Long teacherId;

    @NotNull(message = "ID дисциплины не может быть пустым")
    @Min(value = 1, message = "Некорректный ID дисциплины (должен быть > 0)")
    private Long subjectId;

    @NotNull(message = "ID группы не может быть пустым")
    @Min(value = 1, message = "Некорректный ID группы (должен быть > 0)")
    private Long groupId;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Integer getLessonNumber() { return lessonNumber; }
    public void setLessonNumber(Integer lessonNumber) { this.lessonNumber = lessonNumber; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
}