package hexlet.code.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {

    private Long index;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @NotBlank
    private String title;

    private String content;

    @NotBlank
    private String status;

    private List<Long> taskLabelIds = new ArrayList<>();
}
