package org.example.serveremulator.Controllers;

import jakarta.validation.Valid;
import org.example.serveremulator.DTO.ApiResponse;
import org.example.serveremulator.DTO.GroupRequest;
import org.example.serveremulator.DTO.GroupResponse;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Mappers.GroupMapper;
import org.example.serveremulator.Services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    // смотреть список групп могут все
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getAllGroups() {
        // получаем список и преобразуем в dto
        List<GroupResponse> groups = groupService.getAllGroups().stream()
                .map(groupMapper::toResponse)
                .collect(Collectors.toList());

        // упаковываем в стандартный ответ
        ApiResponse<List<GroupResponse>> response = new ApiResponse<>(true, groups);
        return ResponseEntity.ok(response);
    }

    // смотреть конкретную группу могут все
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<GroupResponse>> getGroup(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        GroupResponse groupResponse = groupMapper.toResponse(group);

        ApiResponse<GroupResponse> response = new ApiResponse<>(true, groupResponse);
        return ResponseEntity.ok(response);
    }

    // создавать группы может только админ
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GroupResponse>> createGroup(@Valid @RequestBody GroupRequest request) {
        // валидация проходит автоматически из-за аннотации @valid
        Group group = groupMapper.toEntity(request);
        Group createdGroup = groupService.createGroup(group);
        GroupResponse groupResponse = groupMapper.toResponse(createdGroup);

        ApiResponse<GroupResponse> response = new ApiResponse<>(true, groupResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // редактировать группы может только админ
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GroupResponse>> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupRequest request) {
        Group group = groupMapper.toEntity(request);
        Group updatedGroup = groupService.updateGroup(id, group);
        GroupResponse groupResponse = groupMapper.toResponse(updatedGroup);

        ApiResponse<GroupResponse> response = new ApiResponse<>(true, groupResponse);
        return ResponseEntity.ok(response);
    }

    // удалять группы может только админ
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);

        // данных для возврата нет, отдаем null
        ApiResponse<Void> response = new ApiResponse<>(true, null);
        return ResponseEntity.ok(response);
    }
}