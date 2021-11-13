package com.tenniscourts.reservations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.guests.CreateGuestDTO;
import com.tenniscourts.guests.UpdateGuestDTO;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateReservationRequestDTO getCreateReservationRequestDTO(long l) {
        return CreateReservationRequestDTO.builder().guestId(1L).scheduleId(l).build();
    }

    @Test
    public void createReservation() throws Exception {
        // Task 1 test
        mockMvc.perform(post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreateReservationRequestDTO(1L)))
        )
                .andDo(print()).andExpect(status().isCreated());

    }

    @Test
    public void availableSlots() throws Exception {
        // Task 2 test
        Map<String, List<String>> result = new HashMap<>();
        result.put("Atlanta Championship", Arrays.asList(
                "2021-11-13T15:00",
                "2021-11-13T16:00",
                "2021-11-13T17:00",
                "2021-11-13T18:00",
                "2021-11-13T19:00",
                "2021-11-13T21:00",
                "2021-11-13T22:00",
                "2021-11-13T23:00"
        ));
        String content = objectMapper.writeValueAsString(result);
        mockMvc.perform(get("/schedule/availability/11-13-2021")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(content));

    }

    @Test
    public void cancelReservation() throws Exception {
        // Task 3 test
        mockMvc.perform(post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreateReservationRequestDTO(2L)))
        )
                .andDo(print()).andExpect(status().isCreated());

        mockMvc.perform(post("/reservation/cancel/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void rescheduleReservation() throws Exception {
        // Task 4 test
        mockMvc.perform(post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreateReservationRequestDTO(3L)))
        )
                .andDo(print()).andExpect(status().isCreated());

        mockMvc.perform(post("/reservation/reschedule/1/2")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void guestTask() throws Exception {
        // Task 5 test

        mockMvc.perform(get("/guest/name/Nadal")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isOk());

        mockMvc.perform(get("/guest/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isOk());


        CreateGuestDTO createGuestDTO = CreateGuestDTO.builder().name("yotan").build();
        mockMvc.perform(post("/guest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGuestDTO))
        )
                .andDo(print()).andExpect(status().isCreated());

        UpdateGuestDTO updateGuestDTO = UpdateGuestDTO.builder().id(1L).name("yotan").build();
        mockMvc.perform(put("/guest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGuestDTO))
        )
                .andDo(print()).andExpect(status().isOk());

        mockMvc.perform(delete("/guest/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isOk());


    }

    //    CreateScheduleRequestDTO
    @Test
    public void createScheduler() throws Exception {
        // Task 6 test
        mockMvc.perform(post("/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateScheduleRequestDTO.builder()
                        .startDateTime(LocalDateTime.now())
                        .tennisCourtId(1L)
                        .build()))
        )
                .andDo(print()).andExpect(status().isCreated());

    }
}
