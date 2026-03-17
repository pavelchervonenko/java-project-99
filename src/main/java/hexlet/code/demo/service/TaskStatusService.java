package hexlet.code.demo.service;

import hexlet.code.demo.dto.TaskStatusCreateDTO;
import hexlet.code.demo.dto.TaskStatusDTO;

import hexlet.code.demo.dto.TaskStatusUpdateDTO;
import hexlet.code.demo.mapper.TaskStatusMapper;

import hexlet.code.demo.repository.TaskStatusRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    private final TaskStatusMapper taskStatusMapper;

    public TaskStatusDTO getTaskStatusById(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task status with id " + id + " not found"));
        return taskStatusMapper.toDTO(taskStatus);
    }

    public List<TaskStatusDTO> getTaskStatusAll() {
        var taskStatuses = taskStatusRepository.findAll();
        return taskStatuses.stream()
                .map(taskStatusMapper::toDTO)
                .toList();
    }

    public TaskStatusDTO createTaskStatus(TaskStatusCreateDTO dto) {
        var taskStatus = taskStatusMapper.toEntity(dto);

        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.toDTO(taskStatus);
    }

    public TaskStatusDTO updateTaskStatus(Long id, TaskStatusUpdateDTO dto) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task status with id " + id + " not found"));

        taskStatusMapper.updateEntityFromDTO(dto, taskStatus);

        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.toDTO(taskStatus);
    }

    public void deleteTaskStatus(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task status with id " + id + " not found"));

        taskStatusRepository.delete(taskStatus);
    }
}
