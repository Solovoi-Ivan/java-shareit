package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));

    @Test
    void getByOwner() throws Exception {
        when(itemService.getByOwner(1, pageRequest)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1)).getByOwner(1, pageRequest);
    }

    @Test
    void getById() throws Exception {
        ItemDtoOutWithBooking itemDtoOut = new ItemDtoOutWithBooking(1, "item", "desc",
                true, null, null, null, null);

        when(itemService.getById(1, 1)).thenReturn(itemDtoOut);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"name\": \"item\",\n" +
                        "\t\"description\": \"desc\",\n" +
                        "\t\"available\": true,\n" +
                        "\t\"requestId\": null,\n" +
                        "\t\"lastBooking\": null,\n" +
                        "\t\"nextBooking\": null,\n" +
                        "\t\"comments\": null\n" +
                        "}"));

        verify(itemService, times(1)).getById(1, 1);
    }

    @Test
    void search() throws Exception {
        when(itemService.search("text", pageRequest)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search?text=text"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1)).search("text", pageRequest);
    }

    @Test
    void create() throws Exception {
        ItemDtoIn itemDtoIn = new ItemDtoIn(1, "item", "desc", true, null);
        ItemDtoOut itemDtoOut = new ItemDtoOut(1, "item", "desc", true, null);

        when(itemService.create(1, itemDtoIn)).thenReturn(itemDtoOut);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"name\": \"item\",\n" +
                        "\t\"description\": \"desc\",\n" +
                        "\t\"available\": true,\n" +
                        "\t\"requestId\": null\n" +
                        "}"));

        verify(itemService, times(1)).create(1, itemDtoIn);
    }

    @Test
    void updateTest() throws Exception {
        ItemDtoIn itemDtoIn = new ItemDtoIn(1, "item", "desc", true, null);
        ItemDtoOut itemDtoOut = new ItemDtoOut(1, "item", "desc", true, null);

        when(itemService.update(1, itemDtoIn)).thenReturn(itemDtoOut);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"name\": \"item\",\n" +
                        "\t\"description\": \"desc\",\n" +
                        "\t\"available\": true,\n" +
                        "\t\"requestId\": null\n" +
                        "}"));

        verify(itemService, times(1)).update(1, itemDtoIn);
    }

    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());

        verify(itemService, times(1)).delete(1);
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDtoIn commentDtoIn = new CommentDtoIn("comment");
        CommentDtoOut commentDtoOut = new CommentDtoOut(1, "comment", "user", null);

        when(itemService.addComment(commentDtoIn, 1, 1)).thenReturn(commentDtoOut);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"text\": \"comment\",\n" +
                        "\t\"authorName\": \"user\",\n" +
                        "\t\"created\": null\n" +
                        "}"));

        verify(itemService, times(1)).addComment(commentDtoIn, 1, 1);
    }
}