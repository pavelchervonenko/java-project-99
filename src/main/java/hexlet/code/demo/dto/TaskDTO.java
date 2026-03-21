package hexlet.code.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskDTO {

    private Long id;

    private String title;

    private Long index;

    private String content;

    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private LocalDateTime createdAt;

    private List<Long> taskLabelIds = new ArrayList<>();
}
