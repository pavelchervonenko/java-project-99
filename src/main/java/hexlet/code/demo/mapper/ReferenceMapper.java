package hexlet.code.demo.mapper;

import hexlet.code.demo.exception.ResourceNotFoundException;

import hexlet.code.demo.model.BaseEntity;
import hexlet.code.demo.model.TaskStatus;

import hexlet.code.demo.repository.TaskStatusRepository;

import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private EntityManager entityManager;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    @Named("toTaskStatus")
    public TaskStatus toTaskStatus(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus not found: " + slug));
    }

    @Named("toSlug")
    public String toSlug(TaskStatus taskStatus) {
        return taskStatus == null ? null : taskStatus.getSlug();
    }
}
