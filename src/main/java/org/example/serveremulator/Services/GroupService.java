package org.example.serveremulator.Services;


import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Repositories.GroupRepository;
import org.example.serveremulator.Repositories.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {//контейнер если null
        if (id==null||id<0) {
            throw new IllegalArgumentException("id is null");
        }
        return groupRepository.findById(id);
    }

    public Group createGroup(Group group) {
        if (group == null || group.getName() == null){
            throw new IllegalArgumentException("name is null or group is null");
        }

        if (groupRepository.existsByName(group.getName())) {
            throw new IllegalArgumentException("group already exists");
        }
        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, Group groupDetails) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Invalid group ID");
        }

        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + id));

        if (groupDetails.getName() != null && !groupDetails.getName().trim().isEmpty()) {
            existingGroup.setName(groupDetails.getName());
        }

        return groupRepository.save(existingGroup);
    }

    public void deleteGroup(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Invalid group ID");
        }

        if (!groupRepository.existsById(id)) {
            throw new IllegalArgumentException("Group not found with id: " + id);
        }

        groupRepository.deleteById(id);
    }

}
