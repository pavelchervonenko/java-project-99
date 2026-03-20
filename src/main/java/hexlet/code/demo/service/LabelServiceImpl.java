package hexlet.code.demo.service;

import hexlet.code.demo.dto.LabelCreateDTO;
import hexlet.code.demo.dto.LabelDTO;
import hexlet.code.demo.dto.LabelUpdateDTO;

import hexlet.code.demo.exception.ResourceNotFoundException;

import hexlet.code.demo.mapper.LabelMapper;

import hexlet.code.demo.repository.LabelRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    private final LabelMapper labelMapper;

    @Override
    @Transactional(readOnly = true)
    public LabelDTO getLabelById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.toDTO(label);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDTO> getAllLabels() {
        var labels = labelRepository.findAll();
        return labels.stream()
                .map(labelMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public LabelDTO createLabel(LabelCreateDTO dto) {
        var label = labelMapper.toEntity(dto);

        labelRepository.save(label);

        return labelMapper.toDTO(label);
    }

    @Override
    @Transactional
    public LabelDTO updateLabel(Long id, LabelUpdateDTO dto) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        labelMapper.updateEntityFromDTO(dto, label);

        labelRepository.save(label);

        return labelMapper.toDTO(label);
    }

    @Override
    @Transactional
    public void deleteLabel(Long id) {
        labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        labelRepository.deleteById(id);
    }
}
