package com.DvFabricio.PicPaySimplificado.controllers;

import com.DvFabricio.PicPaySimplificado.domain.user.User;
import com.DvFabricio.PicPaySimplificado.domain.user.UserType;
import com.DvFabricio.PicPaySimplificado.dtos.UserDTO;
import com.DvFabricio.PicPaySimplificado.services.UserService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create a new user successfully")
    void createUser() {
        UserDTO userDto = new UserDTO("Maria", "Sousa", "99999999901", BigDecimal.TEN, "maria@gmail.com", "12345", UserType.COMMON);
        User newUser = new User(1L, "Maria", "Sousa", "99999999901", "maria@gmail.com", "12345", BigDecimal.TEN, UserType.COMMON);

        when(userService.createUser(any(UserDTO.class))).thenReturn(newUser);

        ResponseEntity<User> response = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser, response.getBody());
        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("Should return all users")
    void getAllUsers() {
        User user1 = new User(1L, "Maria", "Sousa", "99999999901", "maria@gmail.com", "12345", BigDecimal.TEN, UserType.COMMON);
        User user2 = new User(2L, "João", "Sousa", "99999999902", "joao@gmail.com", "12346", BigDecimal.TEN, UserType.COMMON);

        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should find user by document")
    void findUserByDocument() {
        User user = new User(1L, "Maria", "Sousa", "99999999901", "maria@gmail.com", "12345", BigDecimal.TEN, UserType.COMMON);

        when(userService.findUserByDocument("99999999901")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findUserByDocument("99999999901");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).findUserByDocument("99999999901");
    }


    @Test
    @DisplayName("Should return NOT_FOUND when user is not found by document")
    void findUserByDocument_NotFound() {
        when(userService.findUserByDocument("99999999901")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.findUserByDocument("99999999901");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).findUserByDocument("99999999901");
    }
}