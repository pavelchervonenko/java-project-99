package hexlet.code.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.demo.dto.TaskDTO;
import hexlet.code.demo.model.Label;
import hexlet.code.demo.model.Task;
import hexlet.code.demo.model.TaskStatus;
import hexlet.code.demo.model.User;
import hexlet.code.demo.repository.LabelRepository;
import hexlet.code.demo.repository.TaskRepository;
import hexlet.code.demo.repository.TaskStatusRepository;
import hexlet.code.demo.repository.UserRepository;

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
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper om;

    private Task testTask;
    private Task otherTask;
    private TaskStatus testTaskStatus;
    private TaskStatus otherTaskStatus;
    private User testUser;
    private Label testLabel;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();

        testTaskStatus = new TaskStatus();
        testTaskStatus.setName("Draft");
        testTaskStatus.setSlug("draft");
        taskStatusRepository.save(testTaskStatus);

        otherTaskStatus = new TaskStatus();
        otherTaskStatus.setName("Published");
        otherTaskStatus.setSlug("published");
        taskStatusRepository.save(otherTaskStatus);

        testUser = new User();
        testUser.setEmail("user@test.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        testLabel = new Label();
        testLabel.setName("bug");
        labelRepository.save(testLabel);

        testTask = new Task();
        testTask.setName("Fix login bug");
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        testTask.setLabels(List.of(testLabel));
        taskRepository.save(testTask);

        otherTask = new Task();
        otherTask.setName("Add new feature");
        otherTask.setTaskStatus(otherTaskStatus);
        taskRepository.save(otherTask);
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        List<TaskDTO> dtos = om.readValue(body, new TypeReference<>() { });

        var actualIds = dtos.stream().map(TaskDTO::getId).toList();
        var expectedIds = taskRepository.findAll().stream().map(Task::getId).toList();
        assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        var dto = om.readValue(body, TaskDTO.class);

        assertThat(dto.getTitle()).isEqualTo(testTask.getName());
        assertThat(dto.getStatus()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("title", "New Task");
        data.put("status", "draft");

        var request = post("/api/tasks")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());

        var tasks = taskRepository.findAll();
        var created = tasks.stream()
                .filter(t -> t.getName().equals("New Task"))
                .findFirst()
                .orElse(null);

        assertNotNull(created);
        assertThat(created.getTaskStatus().getSlug()).isEqualTo("draft");
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("title", "Updated Task");

        var request = put("/api/tasks/" + testTask.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var updated = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated Task");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }

    @Test
    public void testUpdatePreservesAssignee() throws Exception {
        var data = new HashMap<String, String>();
        data.put("title", "Updated title");

        var request = put("/api/tasks/" + testTask.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var updated = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(updated.getAssignee()).isNotNull();
        assertThat(updated.getAssignee().getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testUpdateAddAssigneeToTaskWithoutOne() throws Exception {
        var data = new HashMap<>();
        data.put("title", otherTask.getName());
        data.put("status", otherTaskStatus.getSlug());
        data.put("assignee_id", testUser.getId());

        var request = put("/api/tasks/" + otherTask.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var updated = taskRepository.findById(otherTask.getId()).orElseThrow();
        assertThat(updated.getAssignee()).isNotNull();
        assertThat(updated.getAssignee().getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testShowUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var data = new HashMap<String, String>();
        // title and status are missing (both required)

        var request = post("/api/tasks")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void testIndexFilterByTitleCont() throws Exception {
        var response = mockMvc.perform(get("/api/tasks").param("titleCont", "login").with(jwt()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<TaskDTO> dtos = om.readValue(response.getContentAsString(), new TypeReference<>() { });

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(testTask.getId());
    }

    @Test
    public void testIndexFilterByStatus() throws Exception {
        var response = mockMvc.perform(get("/api/tasks").param("status", "draft").with(jwt()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<TaskDTO> dtos = om.readValue(response.getContentAsString(), new TypeReference<>() { });

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(testTask.getId());
    }

    @Test
    public void testIndexFilterByAssigneeId() throws Exception {
        var response = mockMvc.perform(get("/api/tasks")
                        .param("assigneeId", testUser.getId().toString()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<TaskDTO> dtos = om.readValue(response.getContentAsString(), new TypeReference<>() { });

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(testTask.getId());
    }

    @Test
    public void testIndexFilterByLabelId() throws Exception {
        var response = mockMvc.perform(get("/api/tasks")
                        .param("labelId", testLabel.getId().toString()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<TaskDTO> dtos = om.readValue(response.getContentAsString(), new TypeReference<>() { });

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(testTask.getId());
    }
}
