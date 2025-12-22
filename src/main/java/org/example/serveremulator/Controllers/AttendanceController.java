package org.example.serveremulator.Controllers;



import org.example.serveremulator.DTO.AttendanceRequest;
import org.example.serveremulator.DTO.AttendanceResponse;
import org.example.serveremulator.Entityes.Attendance;
import org.example.serveremulator.Mappers.AttendanceMapper;
import org.example.serveremulator.Services.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;

    public AttendanceController(AttendanceService attendanceService, AttendanceMapper attendanceMapper) {
        this.attendanceService = attendanceService;
        this.attendanceMapper = attendanceMapper;
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<AttendanceResponse> getAttendanceByLessonId(@PathVariable Long lessonId) {
        // Убрали try-catch, исключения обрабатываются глобально
        return ResponseEntity.ok(attendanceMapper.toResponse(
                attendanceService.getAttendanceByLessonId(lessonId)
        ));
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> createAttendance(@RequestBody AttendanceRequest request) {
        // Убрали try-catch и избыточное создание сущности
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceMapper.toResponse(
                        attendanceService.createAttendance(
                                request.getLessonId(),
                                request.getPresentStudentIds()
                        )
                ));
    }

    @PutMapping("/lesson/{lessonId}")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @PathVariable Long lessonId,
            @RequestBody AttendanceRequest request) {
        // Убрали try-catch
        return ResponseEntity.ok(attendanceMapper.toResponse(
                attendanceService.updateAttendance(
                        lessonId,
                        request.getPresentStudentIds()
                )
        ));
    }

    @DeleteMapping("/lesson/{lessonId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long lessonId) {
        // Убрали try-catch
        attendanceService.deleteAttendance(lessonId);
        return ResponseEntity.noContent().build();
    }
}