package hexlet.code.demo.mapper;

import hexlet.code.demo.dto.UserCreateDTO;
import hexlet.code.demo.dto.UserDTO;
import hexlet.code.demo.dto.UserUpdateDTO;

import hexlet.code.demo.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toEntity(UserCreateDTO dto);

    User toEntity(UserDTO dto);

    UserDTO toDTO(User model);

    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User model);
}
