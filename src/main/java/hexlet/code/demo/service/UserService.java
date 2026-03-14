package hexlet.code.demo.service;

import hexlet.code.demo.dto.UserCreateDTO;
import hexlet.code.demo.dto.UserUpdateDTO;
import hexlet.code.demo.dto.UserDTO;

import hexlet.code.demo.mapper.UserMapper;

import hexlet.code.demo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return userMapper.toDTO(user);

    }

    public List<UserDTO> getUsersAll() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserDTO createUser(UserCreateDTO dto) {
        var user = userMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public UserDTO updateUser(Long id, UserUpdateDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        userMapper.updateEntityFromDTO(dto, user);

        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        userRepository.delete(user);
    }
}
