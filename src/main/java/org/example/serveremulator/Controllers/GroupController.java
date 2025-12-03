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

    @GetMapping ("/{id}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long id) {
        try {
            Group group = groupService.getGroupById(id).
                    orElseThrow(() -> new IllegalArgumentException("Group with id " + id + " not found"));
            return ResponseEntity.ok(groupMapper.toResponse(group));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();

        }
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest request) {
        try
        {
            Group group = groupMapper.toEntity(request);
            Group cratedGroup = groupService.createGroup(group);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(groupMapper.toResponse(cratedGroup));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @RequestBody GroupRequest request) {
        try {
            Group group = groupMapper.toEntity(request);
            Group updatedGroup = groupService.updateGroup(id, group);
            return ResponseEntity.ok(groupMapper.toResponse(updatedGroup));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        try{
            groupService.deleteGroup(id);
            return ResponseEntity.noContent().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


}





















