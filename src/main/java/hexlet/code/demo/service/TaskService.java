package hexlet.code.demo.service;

import hexlet.code.demo.dto.TaskCreateDTO;
import hexlet.code.demo.dto.TaskDTO;
import hexlet.code.demo.dto.TaskParamsDTO;
import hexlet.code.demo.dto.TaskUpdateDTO;

import java.util.List;

public interface TaskService {

    TaskDTO getTaskById(Long id);

    List<TaskDTO> getAllTasks(TaskParamsDTO params);

    TaskDTO createTask(TaskCreateDTO dto);

    TaskDTO updateTask(Long id, TaskUpdateDTO dto);

    void deleteTask(Long id);
}
