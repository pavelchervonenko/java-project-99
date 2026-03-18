package hexlet.code.demo.mapper;

import hexlet.code.demo.dto.LabelCreateDTO;
import hexlet.code.demo.dto.LabelDTO;
import hexlet.code.demo.dto.LabelUpdateDTO;

import hexlet.code.demo.model.Label;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LabelMapper {

    Label toEntity(LabelCreateDTO dto);

    LabelDTO toDTO(Label model);

    void updateEntityFromDTO(LabelUpdateDTO dto, @MappingTarget Label model);
}
