package com.DvFabricio.PicPaySimplificado.services;

import com.DvFabricio.PicPaySimplificado.domain.user.User;
import com.DvFabricio.PicPaySimplificado.domain.user.UserType;
import com.DvFabricio.PicPaySimplificado.dtos.UserDTO;
import com.DvFabricio.PicPaySimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTransactional(User sender, BigDecimal amount) throws Exception {

        if (sender.getUserType().equals(UserType.MERCHANT)) {
            throw new Exception("Usuários lojistas não estão autorizados a enviar transações!");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente.");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuario não encontrado!"));
    }

    public Optional<User> findUserByDocument(String document) {
        return this.repository.findUserByDocument(document);
    }

    public void saveUser(User user){
        this.repository.save(user);
    }

    public User createUser(UserDTO userDto) {
        User newUser = new User(userDto);
        this.saveUser(newUser);
        return  newUser;
    }

    public List<User> getAllUsers() {
       return this.repository.findAll();
    }
}
