package com.DvFabricio.PicPaySimplificado.controllers;

import com.DvFabricio.PicPaySimplificado.domain.transaction.Transaction;
import com.DvFabricio.PicPaySimplificado.dtos.TransactionaDTO;
import com.DvFabricio.PicPaySimplificado.services.TransactionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionalService transactionalService;

    @PostMapping
    public ResponseEntity<Transaction> createTransactional(@RequestBody TransactionaDTO transactionaDto) throws Exception {
        Transaction newTransactional = this.transactionalService.createTransactional(transactionaDto);
        return new ResponseEntity<>(newTransactional, HttpStatus.CREATED);
    }
}
