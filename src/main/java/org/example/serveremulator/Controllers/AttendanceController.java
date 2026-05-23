package org.example.serveremulator.Controllers;



import org.example.serveremulator.DTO.ApiResponse;
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
    public ResponseEntity<ApiResponse<AttendanceResponse>>getAttendanceByLessonId(@PathVariable Long lessonId) {
        Attendance attendance = attendanceService.getAttendanceByLessonId(lessonId);
        AttendanceResponse attendanceResponse = attendanceMapper.toResponse(attendance);

        ApiResponse<AttendanceResponse> response = new ApiResponse<>(true,attendanceResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> createAttendance(@RequestBody AttendanceRequest request) {
        Attendance createdAttendance = attendanceService.createAttendance(
                request.getLessonId(),
                request.getPresentStudentIds()
        );
        AttendanceResponse attendanceResponse = attendanceMapper.toResponse(createdAttendance);

        ApiResponse<AttendanceResponse> apiResponse = new ApiResponse<>(true, attendanceResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(@PathVariable Long lessonId, @RequestBody AttendanceRequest request) {
        Attendance updatedAttendance = attendanceService.updateAttendance(
                lessonId,
                request.getPresentStudentIds()
        );
        AttendanceResponse attendanceResponse = attendanceMapper.toResponse(updatedAttendance);

        ApiResponse<AttendanceResponse> apiResponse = new ApiResponse<>(true, attendanceResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable Long lessonId) {
        attendanceService.deleteAttendance(lessonId);
        ApiResponse<Void> response = new ApiResponse<>(true,null);
        return ResponseEntity.ok(response);
    }
}