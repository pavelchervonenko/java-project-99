package hexlet.code.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {

    @NotBlank
    private String title;

    private String content;

    private List<Long> labelIds = new ArrayList<>();
}
