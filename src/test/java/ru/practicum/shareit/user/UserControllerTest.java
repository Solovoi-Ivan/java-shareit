package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));

    @Test
    void getAllTest() throws Exception {
        when(userService.getAll(pageRequest)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1)).getAll(pageRequest);
    }

    @Test
    void getByIdTest() throws Exception {
        UserDto user = new UserDto(1, "user", "user@mail.ru");

        when(userService.getById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"name\": \"user\",\n" +
                        "\t\"email\": \"user@mail.ru\"\n" +
                        "}"));

        verify(userService, times(1)).getById(1);
    }

    @Test
    void createTest() throws Exception {
        UserDto user = new UserDto(1, "user", "user@mail.ru");

        when(userService.create(user)).thenReturn(user);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"name\": \"user\",\n" +
                        "\t\"email\": \"user@mail.ru\"\n" +
                        "}"));

        verify(userService, times(1)).create(user);
    }

    @Test
    void updateTest() throws Exception {
        UserDto user = new UserDto(1, "user", "user@mail.ru");

        when(userService.getById(1)).thenReturn(user);
        when(userService.update(user)).thenReturn(user);

        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"name\": \"user\",\n" +
                        "\t\"email\": \"user@mail.ru\"\n" +
                        "}"));

        verify(userService, times(1)).update(user);
    }

    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(1);
    }
}