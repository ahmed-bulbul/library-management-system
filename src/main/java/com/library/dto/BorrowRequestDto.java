package com.library.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequestDto {
    @NotNull(message = "Borrower ID is required")
    private Long borrowerId;
    
    @NotNull(message = "Book ID is required")
    private Long bookId;
    
    // Constructors
    public BorrowRequestDto() {}
    
    public BorrowRequestDto(Long borrowerId, Long bookId) {
        this.borrowerId = borrowerId;
        this.bookId = bookId;
    }
    
    // Getters and Setters
    public Long getBorrowerId() { return borrowerId; }
    public void setBorrowerId(Long borrowerId) { this.borrowerId = borrowerId; }
    
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
}