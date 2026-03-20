package hexlet.code.demo.service;

import hexlet.code.demo.dto.LabelCreateDTO;
import hexlet.code.demo.dto.LabelDTO;
import hexlet.code.demo.dto.LabelUpdateDTO;

import java.util.List;

public interface LabelService {

    LabelDTO getLabelById(Long id);

    List<LabelDTO> getAllLabels();

    LabelDTO createLabel(LabelCreateDTO dto);

    LabelDTO updateLabel(Long id, LabelUpdateDTO dto);

    void deleteLabel(Long id);
}
