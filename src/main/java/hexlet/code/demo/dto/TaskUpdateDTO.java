package hexlet.code.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {

    private String title;

    private String content;

    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private Long index;

    private List<Long> taskLabelIds;
}
