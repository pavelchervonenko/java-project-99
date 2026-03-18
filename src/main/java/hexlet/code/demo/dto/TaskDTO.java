package hexlet.code.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDTO {

    private Long id;

    private String title;

    private Long index;

    private String content;

    private String status;

    private Long assigneeId;

    private LocalDateTime createdAt;
}
