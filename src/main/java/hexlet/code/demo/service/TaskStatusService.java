package hexlet.code.demo.service;

import hexlet.code.demo.dto.TaskStatusCreateDTO;
import hexlet.code.demo.dto.TaskStatusDTO;
import hexlet.code.demo.dto.TaskStatusUpdateDTO;

import java.util.List;

public interface TaskStatusService {

    TaskStatusDTO getTaskStatusById(Long id);

    List<TaskStatusDTO> getAllTaskStatuses();

    TaskStatusDTO createTaskStatus(TaskStatusCreateDTO dto);

    TaskStatusDTO updateTaskStatus(Long id, TaskStatusUpdateDTO dto);

    void deleteTaskStatus(Long id);
}
