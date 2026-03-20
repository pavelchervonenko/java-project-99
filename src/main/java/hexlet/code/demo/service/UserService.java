package hexlet.code.demo.service;

import hexlet.code.demo.dto.UserCreateDTO;
import hexlet.code.demo.dto.UserDTO;
import hexlet.code.demo.dto.UserUpdateDTO;

import java.util.List;

public interface UserService {

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    UserDTO createUser(UserCreateDTO dto);

    UserDTO updateUser(Long id, UserUpdateDTO dto);

    void deleteUser(Long id);
}
