package org.example.serveremulator.Controllers;


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
    public List<GroupResponse> getAllGroups() {
        return groupService.getAllGroups().stream()
                .map(groupMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        return ResponseEntity.ok(groupMapper.toResponse(group));
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest request) {
        Group group = groupMapper.toEntity(request);
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(groupMapper.toResponse(createdGroup));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @RequestBody GroupRequest request) {
        Group group = groupMapper.toEntity(request);
        Group updatedGroup = groupService.updateGroup(id, group);
        return ResponseEntity.ok(groupMapper.toResponse(updatedGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}