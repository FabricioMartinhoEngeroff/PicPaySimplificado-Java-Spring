package com.DvFabricio.PicPaySimplificado.repositories;

import com.DvFabricio.PicPaySimplificado.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByDocument(String Document);

    Optional<User> findUserById(Long id);
}
