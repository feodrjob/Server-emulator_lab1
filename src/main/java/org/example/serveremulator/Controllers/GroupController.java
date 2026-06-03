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

    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getAllGroups() {
        //Сначала поулчим список
        List<GroupResponse> groups = groupService.getAllGroups().stream()
                .map(groupMapper::toResponse)
                .collect(Collectors.toList());
        //Упаковали в ApiResponse
        ApiResponse<List<GroupResponse>> response = new ApiResponse<>(true,groups);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> getGroup(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        GroupResponse groupResponse = groupMapper.toResponse(group);

        //Упаковываем
        ApiResponse<GroupResponse> response = new ApiResponse<>(true,groupResponse);
        return ResponseEntity.ok(response);
    }
    /*
    Через аннотацию Valid, указываем, что перед тем как отдать request
    в метод createGroup, нам нужно проверить все аннтоации в DTО
    */
    @PostMapping
    public ResponseEntity<ApiResponse<GroupResponse>> createGroup(@Valid @RequestBody GroupRequest request) {
        Group group = groupMapper.toEntity(request);
        Group createdGroup = groupService.createGroup(group);
        GroupResponse groupResponse = groupMapper.toResponse(createdGroup);

        ApiResponse<GroupResponse> response = new ApiResponse<>(true,groupResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> updateGroup(@PathVariable Long id,@Valid @RequestBody GroupRequest request) {
        Group group = groupMapper.toEntity(request);
        Group updatedGroup = groupService.updateGroup(id, group);
        GroupResponse groupResponse = groupMapper.toResponse(updatedGroup);

        ApiResponse<GroupResponse> response = new ApiResponse<>(true, groupResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);

        //нет поля data, кидаем null
        ApiResponse<Void> response = new ApiResponse<>(true,null);
        return ResponseEntity.ok(response);
    }
}