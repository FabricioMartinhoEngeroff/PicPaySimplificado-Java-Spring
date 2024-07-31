package com.DvFabricio.PicPaySimplificado.controllers;

import com.DvFabricio.PicPaySimplificado.domain.transaction.Transaction;
import com.DvFabricio.PicPaySimplificado.domain.user.User;
import com.DvFabricio.PicPaySimplificado.domain.user.UserType;
import com.DvFabricio.PicPaySimplificado.dtos.TransactionDTO;
import com.DvFabricio.PicPaySimplificado.services.TransactionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class TransactionControllerTest {

    @Mock
    private TransactionalService transactionalService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create a new transaction successfully")
    void createTransactional_Success() throws Exception {
        TransactionDTO transactionDto = new TransactionDTO(BigDecimal.TEN, 1L, 2L);
        User sender = new User(1L, "Maria", "Sousa", "99999999901", "maria@gmail.com", "12345", BigDecimal.TEN, UserType.COMMON);
        User receiver = new User(2L, "João", "Sousa", "99999999902", "joao@gmail.com", "12346", BigDecimal.TEN, UserType.COMMON);
        Transaction newTransaction = new Transaction(1L, BigDecimal.TEN, sender, receiver, LocalDateTime.now());

        when(transactionalService.createTransactional(any(TransactionDTO.class))).thenReturn(newTransaction);

        ResponseEntity<?> response = transactionController.createTransactional(transactionDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newTransaction, response.getBody());
        verify(transactionalService, times(1)).createTransactional(any(TransactionDTO.class));
    }

    @Test
    @DisplayName("Should return BAD_REQUEST when transaction creation fails")
    void createTransactional_Failure() throws Exception {
        TransactionDTO transactionDto = new TransactionDTO(BigDecimal.TEN, 1L, 2L);
        String errorMessage = "Transaction not allowed";

        when(transactionalService.createTransactional(any(TransactionDTO.class))).thenThrow(new Exception(errorMessage));

        ResponseEntity<?> response = transactionController.createTransactional(transactionDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(transactionalService, times(1)).createTransactional(any(TransactionDTO.class));
    }
}