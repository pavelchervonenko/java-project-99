package hexlet.code.demo.specification;

import hexlet.code.demo.dto.TaskParamsDTO;

import hexlet.code.demo.model.Task;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withLabelId(params.getLabelId()))
                .and(withStatus(params.getStatus()));
    }

    private Specification<Task> withTitleCont(String titlePart) {
        return (root, query, cb) -> titlePart == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + titlePart.toLowerCase() + "%");
    }

    private Specification<Task> withAssigneeId(Long id) {
        return (root, query, cb) -> id == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), id);
    }

    private Specification<Task> withLabelId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return cb.conjunction();
            }
            var labels = root.join("labels");
            return cb.equal(labels.get("id"), id);
        };
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("taskStatus").get("slug"), status);
    }
}
