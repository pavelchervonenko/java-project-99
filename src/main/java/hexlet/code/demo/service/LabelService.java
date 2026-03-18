package hexlet.code.demo.service;

import hexlet.code.demo.dto.LabelCreateDTO;
import hexlet.code.demo.dto.LabelDTO;
import hexlet.code.demo.dto.LabelUpdateDTO;

import hexlet.code.demo.exception.ResourceAssociationException;
import hexlet.code.demo.exception.ResourceNotFoundException;

import hexlet.code.demo.mapper.LabelMapper;

import hexlet.code.demo.repository.LabelRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    private final LabelMapper labelMapper;

    @Transactional(readOnly = true)
    public LabelDTO getLabelById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.toDTO(label);
    }

    @Transactional(readOnly = true)
    public List<LabelDTO> getAllLabels() {
        var labels = labelRepository.findAll();
        return labels.stream()
                .map(labelMapper::toDTO)
                .toList();
    }

    @Transactional
    public LabelDTO createLabel(LabelCreateDTO dto) {
        var label = labelMapper.toEntity(dto);

        labelRepository.save(label);

        return labelMapper.toDTO(label);
    }

    @Transactional
    public LabelDTO updateLabel(Long id, LabelUpdateDTO dto) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        labelMapper.updateEntityFromDTO(dto, label);

        labelRepository.save(label);

        return labelMapper.toDTO(label);
    }

    @Transactional
    public void deleteLabel(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        if (!label.getTasks().isEmpty()) {
            throw new ResourceAssociationException("Label with id " + id + " is associated with tasks");
        }

        labelRepository.deleteById(id);
    }
}
