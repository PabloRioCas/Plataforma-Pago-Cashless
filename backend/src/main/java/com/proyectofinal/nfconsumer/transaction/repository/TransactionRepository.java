package com.proyectofinal.nfconsumer.transaction.repository;

import java.util.List;
//import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectofinal.nfconsumer.transaction.controller.TransactionHistoryResponse;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<TransactionHistoryResponse> findAllByUserIdOrderByDateDesc(Integer user_id);
}
