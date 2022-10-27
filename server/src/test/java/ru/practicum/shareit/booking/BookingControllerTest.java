package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.util.BookingState;
import ru.practicum.shareit.util.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());

    @Test
    void addBookingTest() throws Exception {
        BookingDtoIn bookingDtoIn = new BookingDtoIn(1, null, null);
        BookingDtoOut bookingDtoOut = new BookingDtoOut(1, null, null,
                null, null, BookingStatus.WAITING);

        when(bookingService.addBooking(bookingDtoIn, 1)).thenReturn(bookingDtoOut);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"start\": null,\n" +
                        "\t\"end\": null,\n" +
                        "\t\"item\": null,\n" +
                        "\t\"booker\": null,\n" +
                        "\t\"status\": \"WAITING\"\n" +
                        "}"));

        verify(bookingService, times(1)).addBooking(bookingDtoIn, 1);
    }

    @Test
    void approveBookingTest() throws Exception {
        BookingDtoOut bookingDtoOut = new BookingDtoOut(1, null, null,
                null, null, BookingStatus.WAITING);

        when(bookingService.approveBooking(1, 1, true)).thenReturn(bookingDtoOut);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"start\": null,\n" +
                        "\t\"end\": null,\n" +
                        "\t\"item\": null,\n" +
                        "\t\"booker\": null,\n" +
                        "\t\"status\": \"WAITING\"\n" +
                        "}"));

        verify(bookingService, times(1)).approveBooking(1, 1, true);
    }

    @Test
    void getBookingByIdTest() throws Exception {
        BookingDtoOut bookingDtoOut = new BookingDtoOut(1, null, null,
                null, null, BookingStatus.WAITING);

        when(bookingService.getBookingById(1, 1)).thenReturn(bookingDtoOut);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\t\"id\": 1,\n" +
                        "\t\"start\": null,\n" +
                        "\t\"end\": null,\n" +
                        "\t\"item\": null,\n" +
                        "\t\"booker\": null,\n" +
                        "\t\"status\": \"WAITING\"\n" +
                        "}"));

        verify(bookingService, times(1)).getBookingById(1, 1);
    }

    @Test
    void getBookingByBookerTest() throws Exception {
        when(bookingService.getBookingByBooker(1, BookingState.ALL, pageRequest))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings?state=TEST")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(500));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1))
                .getBookingByBooker(1, BookingState.ALL, pageRequest);
    }

    @Test
    void getBookingByOwnerTest() throws Exception {
        when(bookingService.getBookingByOwner(1, BookingState.ALL, pageRequest))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner?state=TEST")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(500));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1))
                .getBookingByOwner(1, BookingState.ALL, pageRequest);
    }
}