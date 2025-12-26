package org.example.serveremulator.Services;


import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.GroupRepository;
import org.example.serveremulator.Repositories.LessonRepository;
import org.example.serveremulator.Repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    public GroupService(GroupRepository groupRepository,
                        StudentRepository studentRepository,
                        LessonRepository lessonRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
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

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Group with id " + id + " not found"
                ));

        studentRepository.deleteByGroupId(id);

        // 2. Удаляем все занятия этой группы
        lessonRepository.deleteByGroupId(id);

        // 3. Удаляем саму группу
        groupRepository.delete(group);
    }

    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return groupRepository.existsById(id);
    }
}