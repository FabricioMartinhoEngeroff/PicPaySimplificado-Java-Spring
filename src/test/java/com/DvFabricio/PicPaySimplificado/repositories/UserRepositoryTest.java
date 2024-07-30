package com.DvFabricio.PicPaySimplificado.repositories;

import com.DvFabricio.PicPaySimplificado.domain.user.User;
import com.DvFabricio.PicPaySimplificado.domain.user.UserType;
import com.DvFabricio.PicPaySimplificado.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Shoud get User successfully from DB")
    void findUserByDocumentCase1() {
        String document = "99999999901";
        UserDTO data = new UserDTO("Fabricio", "test", document, new BigDecimal(10),"test@gmail.com", "444444", UserType.COMMON );
        this.createUser(data);

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();


    }

    @Test
    @DisplayName("Shoud not get User from DB when user not exists")
    void findUserByDocumentCase2() {
        String document = "99999999901";

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();


    }

    @Test
    @DisplayName("Should get User successfully from DB by ID")
    void findUserByIdCase1() {
        UserDTO data = new UserDTO("Fabricio", "test", "99999999901", new BigDecimal(10), "test@gmail.com", "444444", UserType.COMMON);
        User createdUser = this.createUser(data);

        Optional<User> result = this.userRepository.findById(createdUser.getId());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(createdUser.getId());
        assertThat(result.get().getDocument()).isEqualTo(createdUser.getDocument());
    }

    @Test
    @DisplayName("Should not get User from DB when user does not exist by ID")
    void findUserByIdCase2() {
        Long nonExistentId = 999L;

        Optional<User> result = this.userRepository.findById(nonExistentId);

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDTO userDto){
        User newUser = new User(userDto);
        this.entityManager.persist(newUser);
        return newUser;
    }
}