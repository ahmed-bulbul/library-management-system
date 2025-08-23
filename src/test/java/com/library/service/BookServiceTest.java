package com.library.service;

import com.library.dto.BookDto;
import com.library.dto.BorrowRequestDto;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Borrower;
import com.library.exception.BookAlreadyBorrowedException;
import com.library.exception.BookNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BorrowerService borrowerService;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private Borrower testBorrower;
    private BookDto testBookDto;

    @BeforeEach
    void setUp() {
        testBook = new Book("978-0-123456-47-2", "Test Book", "Test Author");
        testBook.setId(1L);

        testBorrower = new Borrower("Test User", "test@email.com");
        testBorrower.setId(1L);

        testBookDto = new BookDto(1L, "978-0-123456-47-2", "Test Book", "Test Author", true);
    }

    @Test
    void registerBook_Success() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Arrays.asList());
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(anyLong())).thenReturn(false);

        BookDto result = bookService.registerBook(testBookDto);

        assertNotNull(result);
        assertEquals(testBookDto.getIsbn(), result.getIsbn());
        assertEquals(testBookDto.getTitle(), result.getTitle());
        assertEquals(testBookDto.getAuthor(), result.getAuthor());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void registerBook_ISBNConflict_ThrowsException() {
        Book existingBook = new Book("978-0-123456-47-2", "Different Title", "Test Author");
        when(bookRepository.findByIsbn(anyString())).thenReturn(Arrays.asList(existingBook));

        assertThrows(RuntimeException.class, () -> bookService.registerBook(testBookDto));
    }

    @Test
    void borrowBook_Success() {
        BorrowRequestDto request = new BorrowRequestDto(1L, 1L);

        when(borrowerService.findBorrowerById(1L)).thenReturn(testBorrower);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(1L)).thenReturn(false);
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenAnswer(invocation -> {
            BorrowRecord record = invocation.getArgument(0);
            record.setId(1L);
            return record;
        });

        BorrowRecord result = bookService.borrowBook(request);

        assertNotNull(result);
        assertEquals(testBorrower, result.getBorrower());
        assertEquals(testBook, result.getBook());
        verify(borrowRecordRepository).save(any(BorrowRecord.class));
    }

    @Test
    void borrowBook_BookAlreadyBorrowed_ThrowsException() {
        BorrowRequestDto request = new BorrowRequestDto(1L, 1L);

        when(borrowerService.findBorrowerById(1L)).thenReturn(testBorrower);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(1L)).thenReturn(true);

        assertThrows(BookAlreadyBorrowedException.class, () -> bookService.borrowBook(request));
    }

    @Test
    void getAllBooks_Success() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(testBook));
        when(borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(anyLong())).thenReturn(false);

        List<BookDto> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testBook.getTitle(), result.get(0).getTitle());
        assertTrue(result.get(0).isAvailable());
    }
}