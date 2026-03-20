package hexlet.code.demo.dto;

import jakarta.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;
}
