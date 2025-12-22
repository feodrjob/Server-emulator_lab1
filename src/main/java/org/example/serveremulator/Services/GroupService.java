package org.example.serveremulator.Services;


import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;
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

    public Group getGroupById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Group ID must be positive number"
            );
        }

        return groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Group with id " + id + " not found"
                ));
    }
    public Group createGroup(Group group) {
        if (group == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Group cannot be null"
            );
        }

        if (group.getName() == null || group.getName().trim().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.GROUP_NAME_EMPTY,
                    "Group name cannot be empty"
            );
        }

        String groupName = group.getName().trim();

        if (groupRepository.existsByName(groupName)) {
            throw new ValidationException(
                    ErrorCode.GROUP_ALREADY_EXISTS,
                    "Group with name '" + groupName + "' already exists"
            );
        }

        group.setName(groupName);
        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, Group groupDetails) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Invalid group ID: " + id
            );
        }

        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Group with id " + id + " not found"
                ));

        if (groupDetails.getName() != null && !groupDetails.getName().trim().isEmpty()) {
            String newName = groupDetails.getName().trim();

            if (!newName.equals(existingGroup.getName())) {
                if (groupRepository.existsByName(newName)) {
                    throw new ValidationException(
                            ErrorCode.GROUP_ALREADY_EXISTS,
                            "Group with name '" + newName + "' already exists"
                    );
                }
                existingGroup.setName(newName);
            }
        }

        return groupRepository.save(existingGroup);
    }

    public void deleteGroup(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Invalid group ID: " + id
            );
        }

        if (!groupRepository.existsById(id)) {
            throw new NotFoundException(
                    ErrorCode.GROUP_NOT_FOUND,
                    "Group with id " + id + " not found"
            );
        }

        groupRepository.deleteById(id);
    }

    //Вспомогательный метод для проверки существования группы
    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return groupRepository.existsById(id);
    }
}