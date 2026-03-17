package hexlet.code.demo.mapper;

import hexlet.code.demo.dto.TaskStatusCreateDTO;
import hexlet.code.demo.dto.TaskStatusUpdateDTO;
import hexlet.code.demo.dto.TaskStatusDTO;

import hexlet.code.demo.model.TaskStatus;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskStatusMapper {

    TaskStatus toEntity(TaskStatusCreateDTO dto);

    TaskStatus toEntity(TaskStatusDTO dto);

    TaskStatusDTO toDTO(TaskStatus model);

    void updateEntityFromDTO(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);
}
