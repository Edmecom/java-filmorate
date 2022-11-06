package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    private static final String PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            "UserLoginEmpty.json",
            "UserEmailDontContain@.json"
    })
    void validateClientError(String filename) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile(String.format("controller/create/request/%s", filename)))
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = "UserSuccess.json")
    void validateSuccess(String filename) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile(String.format("controller/create/request/%s", filename)))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private String getContentFromFile(final String fileName) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + fileName).toPath(),
                    StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to open file", e);
        }
    }
}