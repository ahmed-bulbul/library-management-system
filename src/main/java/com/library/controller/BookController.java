package com.library.controller;

import com.library.dto.BookDto;
import com.library.dto.BorrowRequestDto;
import com.library.entity.BorrowRecord;
import com.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "Endpoints for managing books in the library system")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(
            summary = "Register a new book",
            description = "Registers a new book in the system. " +
                    "If the ISBN already exists, the title and author must match the existing entry.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book registered successfully",
                            content = @Content(schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid book details provided")
            }
    )
    @PostMapping
    public ResponseEntity<BookDto> registerBook(@Valid @RequestBody BookDto bookDto) {
        BookDto registered = bookService.registerBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    @Operation(
            summary = "Get all books",
            description = "Fetches a list of all books currently available in the library.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of books retrieved",
                            content = @Content(schema = @Schema(implementation = BookDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Borrow a book",
            description = "Creates a borrow record for a book by a borrower.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid borrow request or book unavailable")
            }
    )
    @PostMapping("/borrow")
    public ResponseEntity<Map<String, String>> borrowBook(@Valid @RequestBody BorrowRequestDto request) {
        BorrowRecord borrowRecord = bookService.borrowBook(request);
        return ResponseEntity.ok(Map.of(
                "message", "Book borrowed successfully",
                "borrowRecordId", borrowRecord.getId().toString(),
                "borrowedAt", borrowRecord.getBorrowedAt().toString()
        ));
    }

    @Operation(
            summary = "Return a book",
            description = "Marks a borrowed book as returned by the borrower.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book returned successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid return request"),
                    @ApiResponse(responseCode = "404", description = "Borrow record not found")
            }
    )
    @PostMapping("/{bookId}/return")
    public ResponseEntity<Map<String, String>> returnBook(
            @Parameter(description = "ID of the book being returned") @PathVariable Long bookId,
            @Parameter(description = "ID of the borrower returning the book") @RequestParam Long borrowerId) {
        BorrowRecord borrowRecord = bookService.returnBook(bookId, borrowerId);
        return ResponseEntity.ok(Map.of(
                "message", "Book returned successfully",
                "borrowRecordId", borrowRecord.getId().toString(),
                "returnedAt", borrowRecord.getReturnedAt().toString()
        ));
    }
}
