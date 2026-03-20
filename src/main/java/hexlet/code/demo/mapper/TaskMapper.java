package hexlet.code.demo.mapper;

import hexlet.code.demo.dto.TaskCreateDTO;
import hexlet.code.demo.dto.TaskDTO;
import hexlet.code.demo.dto.TaskUpdateDTO;

import hexlet.code.demo.model.Task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "toTaskStatus")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", ignore = true)
    Task toEntity(TaskCreateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus", qualifiedByName = "toSlug")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "labelToIds")
    TaskDTO toDTO(Task model);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", ignore = true)
    void updateEntityFromDTO(TaskUpdateDTO dto, @MappingTarget Task model);
}
