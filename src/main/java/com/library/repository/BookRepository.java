package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE NOT EXISTS " +
           "(SELECT br FROM BorrowRecord br WHERE br.book = b AND br.returnedAt IS NULL)")
    List<Book> findAvailableBooks();
}