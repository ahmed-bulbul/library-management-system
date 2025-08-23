package com.library.service;

import com.library.dto.BorrowerDto;
import com.library.entity.Borrower;
import com.library.exception.BorrowerNotFoundException;
import com.library.repository.BorrowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
class BorrowerServiceTest {

    @Autowired
    private BorrowerService borrowerService;

    @MockitoBean
    private BorrowerRepository borrowerRepository;

    private Borrower borrower;

    @BeforeEach
    void setUp() {
        borrower = new Borrower("John Doe", "john@example.com");
        // simulate DB auto-generated ID
        borrower.setId(1L);
    }

    @Test
    void registerBorrower_success() {
        BorrowerDto inputDto = new BorrowerDto(null, "John Doe", "john@example.com");

        Mockito.when(borrowerRepository.save(any(Borrower.class)))
                .thenReturn(borrower);

        BorrowerDto result = borrowerService.registerBorrower(inputDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");

        Mockito.verify(borrowerRepository).save(any(Borrower.class));
    }

    @Test
    void registerBorrower_duplicateEmail_throwsException() {
        BorrowerDto inputDto = new BorrowerDto(null, "Jane Doe", "john@example.com");

        Mockito.when(borrowerRepository.save(any(Borrower.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> borrowerService.registerBorrower(inputDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Borrower with this email already exists");
    }

    @Test
    void findBorrowerById_success() {
        Mockito.when(borrowerRepository.findById(1L))
                .thenReturn(Optional.of(borrower));

        Borrower found = borrowerService.findBorrowerById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getName()).isEqualTo("John Doe");
    }

    @Test
    void findBorrowerById_notFound_throwsException() {
        Mockito.when(borrowerRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowerService.findBorrowerById(2L))
                .isInstanceOf(BorrowerNotFoundException.class)
                .hasMessage("Borrower with ID 2 not found");
    }
}
