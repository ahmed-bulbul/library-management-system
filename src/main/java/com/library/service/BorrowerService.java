package com.library.service;

import com.library.dto.BorrowerDto;
import com.library.entity.Borrower;
import com.library.exception.BorrowerNotFoundException;
import com.library.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BorrowerService {
    
    @Autowired
    private BorrowerRepository borrowerRepository;
    
    public BorrowerDto registerBorrower(BorrowerDto borrowerDto) {
        try {
            Borrower borrower = new Borrower(borrowerDto.getName(), borrowerDto.getEmail());
            Borrower saved = borrowerRepository.save(borrower);
            return new BorrowerDto(saved.getId(), saved.getName(), saved.getEmail());
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Borrower with this email already exists");
        }
    }
    
    @Transactional(readOnly = true)
    public Borrower findBorrowerById(Long borrowerId) {
        return borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new BorrowerNotFoundException("Borrower with ID " + borrowerId + " not found"));
    }
}