package com.DvFabricio.PicPaySimplificado.services;

import com.DvFabricio.PicPaySimplificado.domain.transaction.Transaction;
import com.DvFabricio.PicPaySimplificado.domain.user.User;
import com.DvFabricio.PicPaySimplificado.dtos.TransactionaDTO;
import com.DvFabricio.PicPaySimplificado.repositories.TransactionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionalService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionalRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransactional(TransactionaDTO transactionDto) throws Exception {
        User sender = this.userService.findUserById(transactionDto.senderId());
        User receiver = this.userService.findUserById(transactionDto.receiverId());

        userService.validateTransactional(sender, transactionDto.value());

        boolean isAuthorize = this.authorizeTransaction(sender,transactionDto.value());
        if (!isAuthorize){
            throw new Exception("Transação não autorizada!");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDto.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestemp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDto.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDto.value()));

        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotificatio(sender, "Transação realizada com sucesso.");
        this.notificationService.sendNotificatio(receiver, "Transação recebida com sucesso.");

        return newTransaction;

    }

    public boolean authorizeTransaction(User user, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("message");
            return  "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }
}
