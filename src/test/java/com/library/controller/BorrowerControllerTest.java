package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.BorrowerDto;
import com.library.service.BorrowerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(BorrowerController.class)
class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BorrowerService borrowerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerBorrower_success() throws Exception {
        BorrowerDto requestDto = new BorrowerDto(null, "John Doe", "john@example.com");
        BorrowerDto responseDto = new BorrowerDto(1L, "John Doe", "john@example.com");

        Mockito.when(borrowerService.registerBorrower(Mockito.any(BorrowerDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    void registerBorrower_invalidRequest_returnsBadRequest() throws Exception {
        // Missing name and email → invalid
        BorrowerDto invalidDto = new BorrowerDto();

        mockMvc.perform(post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
