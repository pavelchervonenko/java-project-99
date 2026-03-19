package hexlet.code.demo.service;

import hexlet.code.demo.dto.TaskCreateDTO;
import hexlet.code.demo.dto.TaskDTO;
import hexlet.code.demo.dto.TaskParamsDTO;
import hexlet.code.demo.dto.TaskUpdateDTO;

import hexlet.code.demo.exception.ResourceNotFoundException;

import hexlet.code.demo.mapper.TaskMapper;

import hexlet.code.demo.repository.LabelRepository;
import hexlet.code.demo.repository.TaskRepository;
import hexlet.code.demo.specification.TaskSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final LabelRepository labelRepository;

    private final TaskMapper taskMapper;

    private final TaskSpecification taskSpecification;

    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.toDTO(task);
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks(TaskParamsDTO params) {
        var spec = taskSpecification.build(params);
        var tasks = taskRepository.findAll(spec);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Transactional
    public TaskDTO createTask(TaskCreateDTO dto) {
        var task = taskMapper.toEntity(dto);

        var labels = labelRepository.findAllById(dto.getLabelIds());
        task.setLabels(labels);

        taskRepository.save(task);

        return taskMapper.toDTO(task);
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskUpdateDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        taskMapper.updateEntityFromDTO(dto, task);

        var labels = labelRepository.findAllById(dto.getLabelIds());
        task.setLabels(labels);

        taskRepository.save(task);

        return taskMapper.toDTO(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }
}
