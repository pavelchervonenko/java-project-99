package hexlet.code.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.demo.dto.LabelDTO;
import hexlet.code.demo.model.Label;
import hexlet.code.demo.model.Task;
import hexlet.code.demo.model.TaskStatus;
import hexlet.code.demo.repository.LabelRepository;
import hexlet.code.demo.repository.TaskRepository;
import hexlet.code.demo.repository.TaskStatusRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper om;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();

        testLabel = new Label();
        testLabel.setName("feature");
        labelRepository.save(testLabel);
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/labels").with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        List<LabelDTO> dtos = om.readValue(body, new TypeReference<>() { });

        var actualIds = dtos.stream().map(LabelDTO::getId).toList();
        var expectedIds = labelRepository.findAll().stream().map(Label::getId).toList();
        assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/labels/" + testLabel.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        var dto = om.readValue(body, LabelDTO.class);

        assertThat(dto.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "bug");

        var request = post("/api/labels")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());

        var created = labelRepository.findByName("bug").orElse(null);

        assertNotNull(created);
        assertThat(created.getName()).isEqualTo("bug");
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "enhancement");

        var request = put("/api/labels/" + testLabel.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var updated = labelRepository.findById(testLabel.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("enhancement");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(labelRepository.findById(testLabel.getId())).isEmpty();
    }

    @Test
    public void testDeleteWithAssociatedTask() throws Exception {
        var taskStatus = new TaskStatus();
        taskStatus.setName("Draft");
        taskStatus.setSlug("draft");
        taskStatusRepository.save(taskStatus);

        var task = new Task();
        task.setName("Test Task");
        task.setTaskStatus(taskStatus);
        task.setLabels(List.of(testLabel));
        taskRepository.save(task);

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(jwt()))
                .andExpect(status().isConflict());

        assertThat(labelRepository.findById(testLabel.getId())).isPresent();
    }

    @Test
    public void testShowUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/labels/" + testLabel.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "ab"); // меньше 3 символов

        var request = post("/api/labels")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }
}
