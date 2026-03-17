package hexlet.code.demo.repository;

import hexlet.code.demo.model.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    TaskStatus findBySlug(String slug);
}
