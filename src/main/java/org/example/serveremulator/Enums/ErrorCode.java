package org.example.serveremulator.Enums;
public enum ErrorCode {
    // ===== ГРУППЫ (1000-1099) =====
    GROUP_NOT_FOUND(1000, "Группа не найдена"),
    GROUP_NAME_EMPTY(1001, "Название группы не может быть пустым"),
    GROUP_ALREADY_EXISTS(1002, "Группа с таким названием уже существует"),
    GROUP_INVALID_ID(1003, "Неверный ID группы"),
    GROUP_HAS_STUDENTS(1004, "Невозможно выполнить операцию - в группе есть студенты"),
    GROUP_HAS_LESSONS(1005, "Невозможно выполнить операцию - у группы есть занятия"),

    // ===== СТУДЕНТЫ (2000-2099) =====
    STUDENT_NOT_FOUND(2000, "Студент не найден"),
    STUDENT_INVALID_NAME(2001, "Неверное имя студента"),
    STUDENT_DELETE_CONFLICT(2002, "Невозможно удалить студента - он есть в записях посещаемости"),
    STUDENT_INVALID_STATUS(2003, "Неверный статус студента"),
    STUDENT_ALREADY_EXISTS(2004, "Студент уже существует в этой группе"),
    STUDENT_NOT_IN_GROUP(2005, "Студент не в указанной группе"),
    STUDENT_INVALID_GROUP(2006, "Неверная группа студента"),

    // ===== ПРЕПОДАВАТЕЛИ (3000-3099) =====
    TEACHER_NOT_FOUND(3000, "Преподаватель не найден"),
    TEACHER_INVALID_NAME(3001, "Неверное ФИО преподавателя"),
    TEACHER_ALREADY_EXISTS(3002, "Преподаватель с таким ФИО уже существует"),
    TEACHER_HAS_LESSONS(3003, "Невозможно выполнить операцию - у преподавателя есть занятия"),
    TEACHER_INVALID_ID(3004, "Неверный ID преподавателя"),

    // ===== ДИСЦИПЛИНЫ (4000-4099) =====
    SUBJECT_NOT_FOUND(4000, "Дисциплина не найдена"),
    SUBJECT_NAME_EMPTY(4001, "Название дисциплины не может быть пустым"),
    SUBJECT_ALREADY_EXISTS(4002, "Дисциплина с таким названием уже существует"),
    SUBJECT_HAS_LESSONS(4003, "Невозможно выполнить операцию - по дисциплине есть занятия"),
    SUBJECT_INVALID_ID(4004, "Неверный ID дисциплины"),

    // ===== ЗАНЯТИЯ (5000-5099) =====
    LESSON_NOT_FOUND(5000, "Занятие не найдено"),
    LESSON_SCHEDULE_CONFLICT(5001, "Конфликт расписания"),
    LESSON_INVALID_DATE(5002, "Неверная дата занятия"),
    LESSON_INVALID_TIME(5003, "Неверное время занятия (должно быть 1-8)"),
    LESSON_MISSING_TEACHER(5004, "Не указан преподаватель"),
    LESSON_MISSING_SUBJECT(5005, "Не указана дисциплина"),
    LESSON_MISSING_GROUP(5006, "Не указана группа"),
    LESSON_INVALID_TEACHER(5007, "Неверный преподаватель для занятия"),
    LESSON_INVALID_SUBJECT(5008, "Неверная дисциплина для занятия"),
    LESSON_INVALID_GROUP(5009, "Неверная группа для занятия"),
    LESSON_DATE_IN_PAST(5010, "Дата занятия не может быть в прошлом"),

    // ===== ПОСЕЩАЕМОСТЬ (6000-6099) =====
    ATTENDANCE_NOT_FOUND(6000, "Запись посещаемости не найдена"),
    ATTENDANCE_ALREADY_EXISTS(6001, "Посещаемость для этого занятия уже существует"),
    STUDENT_NOT_IN_GROUP_FOR_LESSON(6002, "Студент не в группе этого занятия"),
    ATTENDANCE_INVALID_LESSON(6003, "Неверное занятие для посещаемости"),
    ATTENDANCE_EMPTY_STUDENTS(6004, "В посещаемости должен быть хотя бы один студент"),
    ATTENDANCE_DUPLICATE_STUDENTS(6005, "Дублирующиеся студенты в посещаемости"),
    ATTENDANCE_INVALID_STUDENT(6006, "Неверный студент в посещаемости"),

    // ===== ВАЛИДАЦИЯ ДАННЫХ (7000-7099) =====
    VALIDATION_INVALID_ID(7000, "Неверный формат ID"),
    VALIDATION_INVALID_DATE(7001, "Неверный формат даты"),
    VALIDATION_INVALID_STRING(7002, "Неверный формат строки"),
    VALIDATION_REQUIRED_FIELD(7003, "Обязательное поле не заполнено"),
    VALIDATION_OUT_OF_RANGE(7004, "Значение вне допустимого диапазона"),
    VALIDATION_INVALID_EMAIL(7005, "Неверный формат email"),
    VALIDATION_INVALID_PHONE(7006, "Неверный формат телефона"),

    // ===== АВТОРИЗАЦИЯ/ДОСТУП (8000-8099) =====
    UNAUTHORIZED_ACCESS(8000, "Неавторизованный доступ"),
    ACCESS_DENIED(8001, "Доступ запрещен"),
    INVALID_CREDENTIALS(8002, "Неверные учетные данные"),
    TOKEN_EXPIRED(8003, "Срок действия токена истек"),
    TOKEN_INVALID(8004, "Неверный токен аутентификации"),

    // ===== БАЗА ДАННЫХ (8500-8599) =====
    DATABASE_CONNECTION_ERROR(8500, "Ошибка подключения к базе данных"),
    DATABASE_CONSTRAINT_VIOLATION(8501, "Нарушение ограничений базы данных"),
    DATABASE_DEADLOCK(8502, "Обнаружена блокировка базы данных"),
    DATABASE_TIMEOUT(8503, "Таймаут операции с базой данных"),

    // ===== ФАЙЛЫ И ЗАГРУЗКА (8600-8699) =====
    FILE_UPLOAD_ERROR(8600, "Ошибка загрузки файла"),
    FILE_NOT_FOUND(8601, "Файл не найден"),
    FILE_SIZE_EXCEEDED(8602, "Превышен размер файла"),
    FILE_INVALID_FORMAT(8603, "Неверный формат файла"),

    // ===== ВНЕШНИЕ СЕРВИСЫ (8700-8799) =====
    EXTERNAL_SERVICE_ERROR(8700, "Ошибка внешнего сервиса"),
    EXTERNAL_SERVICE_TIMEOUT(8701, "Таймаут внешнего сервиса"),
    EXTERNAL_SERVICE_UNAVAILABLE(8702, "Внешний сервис недоступен"),

    // ===== ОБЩИЕ ОШИБКИ (9000-9999) =====
    VALIDATION_ERROR(9000, "Ошибка валидации"),
    BAD_REQUEST(9001, "Неверный запрос"),
    RESOURCE_NOT_FOUND(9002, "Ресурс не найден"),
    METHOD_NOT_ALLOWED(9003, "Метод не разрешен"),
    CONFLICT(9004, "Конфликт ресурсов"),
    PRECONDITION_FAILED(9005, "Не выполнено предварительное условие"),
    UNSUPPORTED_MEDIA_TYPE(9006, "Неподдерживаемый тип медиа"),
    TOO_MANY_REQUESTS(9007, "Слишком много запросов"),
    INTERNAL_ERROR(9999, "Внутренняя ошибка сервера");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}