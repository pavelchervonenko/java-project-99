package hexlet.code.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {

    @NotBlank
    private String title;

    private String content;
}
