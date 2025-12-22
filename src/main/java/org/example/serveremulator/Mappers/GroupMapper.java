package org.example.serveremulator.Mappers;


import org.example.serveremulator.DTO.GroupRequest;
import org.example.serveremulator.DTO.GroupResponse;
import org.example.serveremulator.Entityes.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {
    public Group toEntity(GroupRequest reqest) {
        Group group = new Group();
        group.setName(reqest.getName());
        return group;
    }

    public GroupResponse toResponse(Group group){
        GroupResponse response = new GroupResponse();
        response.setId(group.getId());
        response.setName(group.getName());
        return response;
    }
}
