package org.example.serveremulator.Enums;


public enum ErrorCode {
    // Группы: 1000-1999
    GROUP_NOT_FOUND(1000, "Group not found"),
    GROUP_NAME_EMPTY(1001, "Group name cannot be empty"),
    GROUP_ALREADY_EXISTS(1002, "Group already exists"),

    // Студенты: 2000-2999
    STUDENT_NOT_FOUND(2000, "Student not found"),
    STUDENT_INVALID_NAME(2001, "Invalid student name"),

    // Преподаватели: 3000-3999
    TEACHER_NOT_FOUND(3000, "Teacher not found"),

    // Дисциплины: 4000-4999
    SUBJECT_NOT_FOUND(4000, "Subject not found"),

    // Занятия: 5000-5999
    LESSON_NOT_FOUND(5000, "Lesson not found"),
    LESSON_SCHEDULE_CONFLICT(5001, "Schedule conflict"),

    // Посещаемость: 6000-6999
    ATTENDANCE_NOT_FOUND(6000, "Attendance not found"),
    ATTENDANCE_ALREADY_EXISTS(6001, "Attendance already exists"),
    STUDENT_NOT_IN_GROUP(6002, "Student not in lesson's group"),

    // Общие: 9000-9999
    VALIDATION_ERROR(9000, "Validation error"),
    INTERNAL_ERROR(9999, "Internal server error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}