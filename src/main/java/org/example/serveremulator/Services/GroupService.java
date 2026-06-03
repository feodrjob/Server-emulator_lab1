package org.example.serveremulator.Services;


import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Group;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.GroupRepository;
import org.example.serveremulator.Repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.GROUP_INVALID_ID,
                    "ID группы должен быть положительным числом"
            );
        }

        return groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Группа с ID " + id + " не найдена"
                ));
    }

    public Group createGroup(Group group) {
        if (group == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Объект группы не может быть null"
            );
        }

        if (group.getName() == null || group.getName().trim().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.GROUP_NAME_EMPTY,
                    "Название группы не может быть пустым"
            );
        }

        String groupName = group.getName().trim();

        if (groupRepository.existsByName(groupName)) {
            throw new ValidationException(
                    ErrorCode.GROUP_ALREADY_EXISTS,
                    "Группа с названием '" + groupName + "' уже существует"
            );
        }

        group.setName(groupName);
        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, Group groupDetails) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.GROUP_INVALID_ID,
                    "Неверный ID группы: " + id
            );
        }

        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Группа с ID " + id + " не найдена"
                ));

        if (groupDetails.getName() != null && !groupDetails.getName().trim().isEmpty()) {
            String newName = groupDetails.getName().trim();

            if (!newName.equals(existingGroup.getName())) {
                if (groupRepository.existsByName(newName)) {
                    throw new ValidationException(
                            ErrorCode.GROUP_ALREADY_EXISTS,
                            "Группа с названием '" + newName + "' уже существует"
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
                    ErrorCode.GROUP_INVALID_ID,
                    "Неверный ID группы: " + id
            );
        }

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "Группа с ID " + id + " не найдена"
                ));

        if (!group.getStudents().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.GROUP_HAS_STUDENTS,
                    "Невозможно удалить группу, так как в ней еще числятся студенты. Колличество: " + group.getStudents().size()
            );
        }

        if (!group.getLessons().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.GROUP_HAS_LESSONS,
                    "Нельзя удалить группу - у нее уже созданы занятия в расписании"
            );

        }

        groupRepository.delete(group);
    }

    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return groupRepository.existsById(id);
    }
}