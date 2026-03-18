package hexlet.code.demo.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {

    private Long index;

    private Long assigneeId;

    @NotBlank
    private String title;

    private String content;

    @NotBlank
    private String status;
}
