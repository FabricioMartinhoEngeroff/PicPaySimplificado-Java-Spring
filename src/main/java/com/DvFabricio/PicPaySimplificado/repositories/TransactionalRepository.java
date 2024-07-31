package com.DvFabricio.PicPaySimplificado.repositories;

import com.DvFabricio.PicPaySimplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionalRepository extends JpaRepository<Transaction, Long> {

}
