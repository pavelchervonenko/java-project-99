package hexlet.code.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.demo.dto.UserDTO;
import hexlet.code.demo.model.Task;
import hexlet.code.demo.model.TaskStatus;
import hexlet.code.demo.model.User;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper om;

    private User testUser;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        List<UserDTO> userDTOS = om.readValue(body, new TypeReference<>() { });

        var actualIds = userDTOS.stream().map(UserDTO::getId).toList();
        var expectedIds = userRepository.findAll().stream().map(User::getId).toList();
        assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/users/" + testUser.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = response.getContentAsString();
        var userDTO = om.readValue(body, UserDTO.class);

        assertThat(userDTO.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(userDTO.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("firstName", "Jane");
        data.put("lastName", "Smith");
        data.put("email", "jane@example.com");
        data.put("password", "secret123");

        var request = post("/api/users")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());

        var users = userRepository.findAll();
        var createdUser = users.stream()
                .filter(u -> u.getEmail().equals("jane@example.com"))
                .findFirst()
                .orElse(null);

        assertNotNull(createdUser);
        assertThat(createdUser.getFirstName()).isEqualTo("Jane");
        assertThat(createdUser.getLastName()).isEqualTo("Smith");
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("email", "jack@yahoo.com");
        data.put("password", "new-password");

        var request = put("/api/users/" + testUser.getId())
                .with(jwt().jwt(j -> j.subject("john@example.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(user.getEmail()).isEqualTo("jack@yahoo.com");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId())
                        .with(jwt().jwt(j -> j.subject("john@example.com"))))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }

    @Test
    public void testUpdateByAnotherUser() throws Exception {
        var data = new HashMap<String, String>();
        data.put("email", "hacker@example.com");

        var request = put("/api/users/" + testUser.getId())
                .with(jwt().jwt(j -> j.subject("another@example.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isForbidden());
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
        task.setAssignee(testUser);
        taskRepository.save(task);

        mockMvc.perform(delete("/api/users/" + testUser.getId())
                        .with(jwt().jwt(j -> j.subject("john@example.com"))))
                .andExpect(status().isConflict());

        assertThat(userRepository.findById(testUser.getId())).isPresent();
    }
}
