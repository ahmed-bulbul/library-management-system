package com.library.service;

import com.library.dto.BookDto;
import com.library.dto.BorrowRequestDto;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Borrower;
import com.library.exception.BookAlreadyBorrowedException;
import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidBookRegistrationException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    
    @Autowired
    private BorrowerService borrowerService;
    
    public BookDto registerBook(BookDto bookDto) {

        Optional<Book> existingBookOpt = bookRepository.findByIsbn(bookDto.getIsbn())
                .stream()
                .findFirst();

        existingBookOpt.ifPresent(existing -> {
            if (!existing.getTitle().equals(bookDto.getTitle()) ||
                    !existing.getAuthor().equals(bookDto.getAuthor())) {
                throw new InvalidBookRegistrationException(
                        "Books with the same ISBN must have the same title and author"
                );
            }
        });

        Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), bookDto.getAuthor());
        return convertToDto(bookRepository.save(book));
    }
    
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BorrowRecord borrowBook(BorrowRequestDto request) {
        Borrower borrower = borrowerService.findBorrowerById(request.getBorrowerId());
        Book book = findBookById(request.getBookId());
        
        if (borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(request.getBookId())) {
            throw new BookAlreadyBorrowedException(
                "Book with ID " + request.getBookId() + " is already borrowed"
            );
        }
        
        BorrowRecord borrowRecord = new BorrowRecord(borrower, book);
        return borrowRecordRepository.save(borrowRecord);
    }
    
    public BorrowRecord returnBook(Long bookId, Long borrowerId) {
        BorrowRecord borrowRecord = borrowRecordRepository
                .findByBookIdAndReturnedAtIsNull(bookId)
                .orElseThrow(() -> new BookNotFoundException(
                    "No active borrow record found for book ID " + bookId
                ));
        
        if (!borrowRecord.getBorrower().getId().equals(borrowerId)) {
            throw new RuntimeException("Book can only be returned by the borrower");
        }
        
        borrowRecord.setReturnedAt(LocalDateTime.now());
        return borrowRecordRepository.save(borrowRecord);
    }
    
    @Transactional(readOnly = true)
    public Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
    }
    
    private BookDto convertToDto(Book book) {
        boolean available = !borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(book.getId());
        return new BookDto(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), available);
    }
}