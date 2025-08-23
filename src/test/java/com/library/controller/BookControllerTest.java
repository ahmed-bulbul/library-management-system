package com.library.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.BookDto;
import com.library.dto.BorrowRequestDto;
import com.library.entity.BorrowRecord;
import com.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerBook_Success() throws Exception {
        BookDto bookDto = new BookDto(null, "978-0-123456-47-2", "Test Book", "Test Author", true);
        BookDto savedBook = new BookDto(1L, "978-0-123456-47-2", "Test Book", "Test Author", true);

        when(bookService.registerBook(any(BookDto.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-0-123456-47-2"))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"));
    }

    @Test
    void getAllBooks_Success() throws Exception {
        BookDto book1 = new BookDto(1L, "978-0-123456-47-2", "Book 1", "Author 1", true);
        BookDto book2 = new BookDto(2L, "978-0-123456-48-9", "Book 2", "Author 2", false);

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));
    }

    @Test
    void borrowBook_Success() throws Exception {
        BorrowRequestDto request = new BorrowRequestDto(1L, 1L);
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(1L);
        borrowRecord.setBorrowedAt(LocalDateTime.now());

        when(bookService.borrowBook(any(BorrowRequestDto.class))).thenReturn(borrowRecord);

        mockMvc.perform(post("/api/v1/books/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book borrowed successfully"))
                .andExpect(jsonPath("$.borrowRecordId").value("1"));
    }

    @Test
    void registerBook_ValidationError() throws Exception {
        BookDto invalidBook = new BookDto(null, "", "", "", true); // Empty required fields

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}