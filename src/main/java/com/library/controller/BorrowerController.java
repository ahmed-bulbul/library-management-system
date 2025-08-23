package com.library.controller;

import com.library.dto.BorrowerDto;
import com.library.service.BorrowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/borrowers")
@Tag(name = "Borrowers", description = "Endpoints for managing borrowers in the library system")
public class BorrowerController {

    @Autowired
    private BorrowerService borrowerService;

    @Operation(
            summary = "Register a new borrower",
            description = "Registers a new borrower (library member) into the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Borrower registered successfully",
                            content = @Content(schema = @Schema(implementation = BorrowerDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid borrower details provided")
            }
    )
    @PostMapping
    public ResponseEntity<BorrowerDto> registerBorrower(@Valid @RequestBody BorrowerDto borrowerDto) {
        BorrowerDto registered = borrowerService.registerBorrower(borrowerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }
}
