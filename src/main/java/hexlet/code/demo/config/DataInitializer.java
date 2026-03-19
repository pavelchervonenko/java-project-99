package hexlet.code.demo.config;

import hexlet.code.demo.model.Label;
import hexlet.code.demo.model.TaskStatus;
import hexlet.code.demo.model.User;

import hexlet.code.demo.repository.LabelRepository;
import hexlet.code.demo.repository.TaskStatusRepository;
import hexlet.code.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final TaskStatusRepository taskStatusRepository;

    private final LabelRepository labelRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            var user = new User();
            user.setFirstName("admin");
            user.setLastName("admin");
            user.setEmail("hexlet@example.com");
            user.setPassword(passwordEncoder.encode(adminPassword));
            userRepository.save(user);
        }

        var defaultStatuses = Map.of(
                "draft", "Draft",
                "to_review", "To Review",
                "to_be_fixed", "To Be Fixed",
                "to_publish", "To Publish",
                "published", "Published"
        );

        defaultStatuses.forEach((slug, name) -> {
            if (taskStatusRepository.findBySlug(slug).isEmpty()) {
                var status = new TaskStatus();
                status.setName(name);
                status.setSlug(slug);
                taskStatusRepository.save(status);
            }
        });

        var defaultLabels = List.of("feature", "bug");

        defaultLabels.forEach(name -> {
            if (labelRepository.findByName(name).isEmpty()) {
                var label = new Label();
                label.setName(name);
                labelRepository.save(label);
            }
        });
    }
}
