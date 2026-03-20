package hexlet.code.demo.service;

import hexlet.code.demo.dto.TaskStatusCreateDTO;
import hexlet.code.demo.dto.TaskStatusDTO;
import hexlet.code.demo.dto.TaskStatusUpdateDTO;

import hexlet.code.demo.exception.ResourceNotFoundException;

import hexlet.code.demo.mapper.TaskStatusMapper;

import hexlet.code.demo.repository.TaskStatusRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    private final TaskStatusMapper taskStatusMapper;

    @Override
    @Transactional(readOnly = true)
    public TaskStatusDTO getTaskStatusById(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));
        return taskStatusMapper.toDTO(taskStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatusDTO> getAllTaskStatuses() {
        var taskStatuses = taskStatusRepository.findAll();
        return taskStatuses.stream()
                .map(taskStatusMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public TaskStatusDTO createTaskStatus(TaskStatusCreateDTO dto) {
        var taskStatus = taskStatusMapper.toEntity(dto);

        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.toDTO(taskStatus);
    }

    @Override
    @Transactional
    public TaskStatusDTO updateTaskStatus(Long id, TaskStatusUpdateDTO dto) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));

        taskStatusMapper.updateEntityFromDTO(dto, taskStatus);

        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.toDTO(taskStatus);
    }

    @Override
    @Transactional
    public void deleteTaskStatus(Long id) {
        taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));

        taskStatusRepository.deleteById(id);
    }
}
