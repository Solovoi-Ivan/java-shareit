package ru.practicum.shareit.requests;

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
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.ItemRequestDtoOut;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("creationDate"));

    @Test
    void createTest() throws Exception {
        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn("desc");
        ItemRequestDtoOut itemRequestDtoOut = new ItemRequestDtoOut(1, "desc", null, null);

        when(itemRequestService.create(itemRequestDtoIn, 1)).thenReturn(itemRequestDtoOut);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"description\": \"desc\",\n" +
                        "\t\"created\": null,\n" +
                        "\t\"items\": null\n" +
                        "}"));

        verify(itemRequestService, times(1)).create(itemRequestDtoIn, 1);
    }

    @Test
    void getByRequesterTest() throws Exception {
        when(itemRequestService.getByRequester(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).getByRequester(1);
    }

    @Test
    void getAllTest() throws Exception {
        when(itemRequestService.getAll(1, pageRequest)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).getAll(1, pageRequest);
    }

    @Test
    void getByIdTest() throws Exception {
        ItemRequestDtoOut itemRequestDtoOut = new ItemRequestDtoOut(1, "desc", null, null);

        when(itemRequestService.getById(1, 1)).thenReturn(itemRequestDtoOut);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"description\": \"desc\",\n" +
                        "\t\"created\": null,\n" +
                        "\t\"items\": null\n" +
                        "}"));

        verify(itemRequestService, times(1)).getById(1, 1);
    }
}