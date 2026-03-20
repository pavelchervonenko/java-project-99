package hexlet.code.demo.controller;

import hexlet.code.demo.model.Task;
import hexlet.code.demo.repository.TaskRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.demo.dto.TaskStatusDTO;
import hexlet.code.demo.model.TaskStatus;
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
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    private TaskStatus testTaskStatus;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();

        testTaskStatus = new TaskStatus();
        testTaskStatus.setName("Draft");
        testTaskStatus.setSlug("draft");
        taskStatusRepository.save(testTaskStatus);
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        List<TaskStatusDTO> dtos = om.readValue(body, new TypeReference<>() { });

        var actualIds = dtos.stream().map(TaskStatusDTO::getId).toList();
        var expectedIds = taskStatusRepository.findAll().stream().map(TaskStatus::getId).toList();
        assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses/" + testTaskStatus.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        var dto = om.readValue(body, TaskStatusDTO.class);

        assertThat(dto.getName()).isEqualTo(testTaskStatus.getName());
        assertThat(dto.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "Published");
        data.put("slug", "published");

        var request = post("/api/task_statuses")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());

        var statuses = taskStatusRepository.findAll();
        var created = statuses.stream()
                .filter(s -> s.getSlug().equals("published"))
                .findFirst()
                .orElse(null);

        assertNotNull(created);
        assertThat(created.getName()).isEqualTo("Published");
        assertThat(created.getSlug()).isEqualTo("published");
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "Updated Name");

        var request = put("/api/task_statuses/" + testTaskStatus.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var updated = taskStatusRepository.findById(testTaskStatus.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.findById(testTaskStatus.getId())).isEmpty();
    }

    @Test
    public void testDeleteWithAssociatedTask() throws Exception {
        var task = new Task();
        task.setName("Test Task");
        task.setTaskStatus(testTaskStatus);
        taskRepository.save(task);

        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()).with(jwt()))
                .andExpect(status().isConflict());

        assertThat(taskStatusRepository.findById(testTaskStatus.getId())).isPresent();
    }
}
