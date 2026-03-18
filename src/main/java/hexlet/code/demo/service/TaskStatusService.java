package hexlet.code.demo.service;

import hexlet.code.demo.dto.TaskStatusCreateDTO;
import hexlet.code.demo.dto.TaskStatusDTO;

import hexlet.code.demo.dto.TaskStatusUpdateDTO;
import hexlet.code.demo.exception.ResourceAssociationException;
import hexlet.code.demo.exception.ResourceNotFoundException;
import hexlet.code.demo.mapper.TaskStatusMapper;

import hexlet.code.demo.repository.TaskStatusRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    private final TaskStatusMapper taskStatusMapper;

    @Transactional(readOnly = true)
    public TaskStatusDTO getTaskStatusById(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));
        return taskStatusMapper.toDTO(taskStatus);
    }

    @Transactional(readOnly = true)
    public List<TaskStatusDTO> getAllTaskStatuses() {
        var taskStatuses = taskStatusRepository.findAll();
        return taskStatuses.stream()
                .map(taskStatusMapper::toDTO)
                .toList();
    }

    @Transactional
    public TaskStatusDTO createTaskStatus(TaskStatusCreateDTO dto) {
        var taskStatus = taskStatusMapper.toEntity(dto);

        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.toDTO(taskStatus);
    }

    @Transactional
    public TaskStatusDTO updateTaskStatus(Long id, TaskStatusUpdateDTO dto) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));

        taskStatusMapper.updateEntityFromDTO(dto, taskStatus);

        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.toDTO(taskStatus);
    }

    @Transactional
    public void deleteTaskStatus(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));

        if (!taskStatus.getTasksList().isEmpty()) {
            throw new ResourceAssociationException("Task status with id " + id + " is associated with tasks");
        }

        taskStatusRepository.delete(taskStatus);
    }
}
