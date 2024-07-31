package com.DvFabricio.PicPaySimplificado.services;

import com.DvFabricio.PicPaySimplificado.domain.user.User;
import com.DvFabricio.PicPaySimplificado.domain.user.UserType;
import com.DvFabricio.PicPaySimplificado.dtos.TransactionDTO;
import com.DvFabricio.PicPaySimplificado.repositories.TransactionalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionalServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionalRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    TransactionalService transactionalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is ok ")
    void createTransactionalCase1() throws Exception {

        User sender = new User(1l, "Maria", "Sousa", "99999999901", "maria@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2l, "João", "Sousa", "99999999902", "joao@gmail.com", "12346", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1l)).thenReturn(sender);
        when(userService.findUserById(2l)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1l, 2l);
        transactionalService.createTransactional(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso");
        verify(notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso");
    }


    @Test
    @DisplayName("Should throw Exception when transaction is not allowed")
    void createTransactionalCase2() throws Exception {

        User sender = new User(1L, "Maria", "Sousa", "99999999901", "maria@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "João", "Sousa", "99999999902", "joao@gmail.com", "12346", new BigDecimal(10), UserType.COMMON);
        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);


        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            transactionalService.createTransactional(request);
        });

        assertNotNull(exception);
    }
}